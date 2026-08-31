package com.example.sistemaidentidaddigital.config;

import com.example.sistemaidentidaddigital.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final LoginAuthenticationHandler loginAuthenticationHandler;

    public SecurityConfig(
            CustomUserDetailsService userDetailsService,
            LoginAuthenticationHandler loginAuthenticationHandler) {

        this.userDetailsService = userDetailsService;
        this.loginAuthenticationHandler = loginAuthenticationHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            PasswordEncoder passwordEncoder) throws Exception {

        http
            .userDetailsService(userDetailsService)

            .authorizeHttpRequests(auth -> auth
.requestMatchers("/login", "/registro", "/css/**", "/images/**").permitAll()
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(loginAuthenticationHandler)
                .failureHandler(loginAuthenticationHandler)
                .permitAll()
            )

            .logout(logout -> logout
                .logoutSuccessUrl("/login")
                .permitAll()
            );

        return http.build();
    }
}