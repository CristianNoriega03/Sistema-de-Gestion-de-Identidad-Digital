package com.example.sistemaidentidaddigital.config;

import com.example.sistemaidentidaddigital.util.LoginManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginAuthenticationHandler
        implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

    private final LoginManager loginManager;

    public LoginAuthenticationHandler() {
        this.loginManager = LoginManager.getInstance();
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        loginManager.registrarInicioSesion(authentication.getName());

        response.sendRedirect("/");
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            org.springframework.security.core.AuthenticationException exception)
            throws IOException, ServletException {

        String email = request.getParameter("username");

        loginManager.registrarIntentoFallido(email);

        response.sendRedirect("/login?error=true");
    }
}