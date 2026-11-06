package com.example.sistemaidentidaddigital.config;

import com.example.sistemaidentidaddigital.mediator.Mediador;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginAuthenticationHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

    // El Handler ahora solo conoce al Mediador (La torre de control)
    private final Mediador mediador;

    public LoginAuthenticationHandler(Mediador mediador) {
        this.mediador = mediador;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        // Delegamos toda la lógica de éxito al mediador
        mediador.notificarEvento("EXITO", authentication.getName(), request.getSession());
        
        // Redirigimos a la página de inicio
        response.sendRedirect("/");
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        
        String email = request.getParameter("username");
        
        // Delegamos toda la lógica de fallo al mediador
        mediador.notificarEvento("FALLO", email, request.getSession());
        
        // Redirigimos al login con el mensaje de error
        response.sendRedirect("/login?error=true");
    }
}