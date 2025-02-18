package com.example.codoceanb.infras.service;

import com.example.codoceanb.auth.entity.OTP;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {
    @Value("${third-party.sending-email.address}")
    private String from;

    @Value("${third-party.sending-email.password}")
    private String password;
    @Override
    public void sendHtmlContent(String toEmail, String subject, String htmlBody) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message emailMessage = new MimeMessage(session);
            emailMessage.setFrom(new InternetAddress(from));
            emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            emailMessage.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(htmlBody, "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            emailMessage.setContent(multipart);

            Transport transport = session.getTransport();
            transport.connect();
            transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String createHtmlEmailContentWithOTP(String otpString, OTP.EType type) {
        String typeString = type.equals(OTP.EType.ACTIVE_ACCOUNT)?
                "activate your account":
                "reset your password";
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>OTP Verification</title>"
                + "<style>"
                + "body {"
                + "    font-family: Arial, sans-serif;"
                + "    margin: 0;"
                + "    padding: 0;"
                + "    background-color: #f4f4f4;"
                + "    text-align: center;"
                + "}"
                + ".container {"
                + "    max-width: 600px;"
                + "    margin: 20px auto;"
                + "    padding: 20px;"
                + "    background-color: #ffffff;"
                + "    border-radius: 5px;"
                + "    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);"
                + "}"
                + ".header {"
                + "    margin-bottom: 20px;"
                + "}"
                + ".otp {"
                + "    font-size: 24px;"
                + "    font-weight: bold;"
                + "    color: #333333;"
                + "}"
                + ".footer {"
                + "    margin-top: 20px;"
                + "    font-size: 14px;"
                + "    color: #666666;"
                + "}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<div class=\"header\">"
                + "<h2>Your OTP Code</h2>"
                + "</div>"
                + "<p>Hello,</p>"
                + "<p>Here is your One-Time Password (OTP) to " + typeString + ":</p>"
                + "<p class=\"otp\">" + otpString + "</p>"
                + "<p>Please enter this OTP in the application to complete the process.</p>"
                + "<div class=\"footer\">"
                + "<p>If you did not request this, please ignore this email.</p>"
                + "<p>Thank you!</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }
}
