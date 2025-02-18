package com.example.codoceanb.infras.security;

import com.example.codoceanb.auth.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    private static final Logger log = LogManager.getLogger(JwtTokenUtils.class);

    private final String secretKey;

    public JwtTokenUtils() {
        this.secretKey = generateSecretKey();
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("isActive", user.isActive());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() +  Duration.ofMinutes(30).toMillis()))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, User userDetails) throws Exception {
        String email = extractEmail(token);
        boolean isActive = extractIsActive(token);
        return !userDetails.isLocked() &&
                isValidToken(token) &&
                email.equals(userDetails.getEmail()) &&
                isActive;
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    private String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256-bit key
        random.nextBytes(keyBytes);
        return Encoders.BASE64.encode(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValidToken(String token) throws Exception {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Lỗi xác thực token: " + e.getMessage());
            throw new Exception("Lỗi xác thực token: " + e.getMessage());
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

     public boolean extractIsActive(String token) {
        return extractClaim(token, claims -> claims.get("isActive", Boolean.class));
    }

    public String extractEmailFromBearerToken(String token) {
        return extractEmail(token.substring(7));
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + Duration.ofDays(7).toMillis()))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
