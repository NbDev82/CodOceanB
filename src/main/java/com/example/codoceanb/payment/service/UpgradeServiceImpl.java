package com.example.codoceanb.payment.service;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.exception.UserNotFoundException;
import com.example.codoceanb.auth.repository.UserRepos;
import com.example.codoceanb.auth.utils.MessageKeys;
import com.example.codoceanb.infras.security.JwtTokenUtils;
import com.example.codoceanb.payment.dto.PaymentDTO;
import com.example.codoceanb.payment.entity.Payment;
import com.example.codoceanb.payment.request.UpgradeRequest;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.*;
import com.paypal.payments.CaptureRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UpgradeServiceImpl implements UpgradeService{

    @Value("${third-party.paypal.returnUrl}")
    private String returnUrl;

    @Value("${third-party.paypal.cancelUrl}")
    private String cancelUrl;

    @Autowired
    private PayPalHttpClient payPalClient;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private UserRepos userRepos;

     @Override
     public String processUpgradeRequest(UpgradeRequest upgradeRequest) {
         OrdersCreateRequest request = new OrdersCreateRequest();
         request.requestBody(buildRequestBody(upgradeRequest));

         try {
             Order order = payPalClient.execute(request).result();
             return order.links().get(1).href();
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
     }

    @Override
    public String executePayment(String authHeader, String token, String payerId) {
        try {
            String result;
            OrdersGetRequest request = new OrdersGetRequest(token);
            Order order = payPalClient.execute(request).result();

            if ("APPROVED".equals(order.status())) {
                result = capturePayment(order.id());
                if(MessageKeys.CAPTURE_PAYPAL_SUCCESSFUL.equals(result)) {
                    String durationInMonths = order.purchaseUnits().get(0).description();
                    String email = jwtTokenUtils.extractEmailFromBearerToken(authHeader);
                    User user = userRepos.findByEmail(email)
                            .orElseThrow(() -> new UserNotFoundException("User not found!"));
                    LocalDateTime time = (user.getVIPExpDate() == null || LocalDateTime.now().isBefore(user.getVIPExpDate())) ?
                            LocalDateTime.now().plusMonths(Long.parseLong(durationInMonths)) :
                            user.getVIPExpDate().plusMonths(Long.parseLong(durationInMonths));
                    upgradeVIP(user, time, order);
                }
            } else {
                result = MessageKeys.CAPTURE_PAYPAL_FAILED;
            }

            return result;
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public PaymentDTO getInfoOrder(String token) {
        try {
            OrdersGetRequest request = new OrdersGetRequest(token);
            Order order = payPalClient.execute(request).result();
            return convertOrderDTO(order);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PaymentDTO convertOrderDTO(Order order) {
        return PaymentDTO.builder()
                .id(order.id())
                .paymentMethod("PayPal")
                .paymentStatus(order.status())
                .paymentDate(order.createTime())
                .amount(order.purchaseUnits().get(0).amountWithBreakdown().value())
                .currency(order.purchaseUnits().get(0).amountWithBreakdown().currencyCode())
                .description(order.purchaseUnits().get(0).description())
                .build();
    }

    private void upgradeVIP(User user, LocalDateTime VIPExpDate, Order order) {
        Payment payment = Payment.builder()
                .id(order.id())
                .amount(Double.parseDouble(order.purchaseUnits().get(0).amountWithBreakdown().value()))
                .currency(order.purchaseUnits().get(0).amountWithBreakdown().currencyCode())
                .paymentDate(LocalDateTime.now())
                .paymentMethod("PayPal")
                .paymentStatus("COMPLETED")
                .build();

        if (user.getPayments().isEmpty()) {
            List<Payment> payments = new ArrayList<>();
            payment.setUser(user);
            payments.add(payment);
            user.setPayments(payments);
        } else {
            user.getPayments().add(payment);
        }

        user.setVIPExpDate(VIPExpDate);
        user.setUpdatedAt(LocalDateTime.now());
        userRepos.save(user);
    }

    public String capturePayment(String orderId) {
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
        request.requestBody(new CaptureRequest());

        try {
            Order capture = payPalClient.execute(request).result();
            return "COMPLETED".equals(capture.status())?
                    MessageKeys.CAPTURE_PAYPAL_SUCCESSFUL:
                    MessageKeys.CAPTURE_PAYPAL_FAILED;
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    private OrderRequest buildRequestBody(UpgradeRequest upgradeRequest) {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        ApplicationContext applicationContext = new ApplicationContext()
                .brandName("CodOcean")
                .landingPage("BILLING")
                .cancelUrl(cancelUrl)
                .returnUrl(returnUrl);

        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();

        // Thêm item vào đơn hàng
        purchaseUnits.add(new PurchaseUnitRequest()
                .amountWithBreakdown(new AmountWithBreakdown()
                        .currencyCode("USD")
                        .value(String.valueOf(upgradeRequest.getFee())))
                .description(upgradeRequest.getDurationInMonths())
                .referenceId("VIP"+ upgradeRequest.getDurationInMonths() ));

        orderRequest.applicationContext(applicationContext);
        orderRequest.purchaseUnits(purchaseUnits);

        return orderRequest;
    }
}
