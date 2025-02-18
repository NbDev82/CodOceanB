package com.example.codoceanb.infras.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${frontend.mobile.url}")
    private String frontendMobileUrl;

    @Autowired
    @Lazy
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(
                                "/ws/**",
                                "/api/auth/v1/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html")
                        .permitAll()
                        .requestMatchers(
                                "/api/v1/code/**",
                                "/api/discusses/**",
                                "/api/v1/react/discuss/**",
                                "/api/topics/**",
                                "/api/search/**",
                                "/api/trending/**",
                                "/v1/api/payment-paypal/**",
                                "/v1/api/payment/**",
                                "/v1/api/payment-info/**",
                                "/api/v1/upload/**",
                                "/api/v1/discuss/comments/**",
                                "/api/v1/reports/**")
                        .hasAnyRole("USER", "USER_VIP")
                        .requestMatchers(
                                "/api/admin/**")
                        .hasRole("ADMIN")
                        .requestMatchers(
                                "/api/profile/**",
                                "/api/user/**",
                                "/api/v1/discuss/categories/**")
                        .hasAnyRole("USER", "ADMIN", "USER_VIP", "MODERATOR")
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        List<String> frontEndUrls = Arrays.stream(frontendUrl.split(",")).toList();
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.addAllowedOrigin(frontendUrl);
        configuration.setAllowedOriginPatterns(frontEndUrls);
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin(frontendMobileUrl);
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}