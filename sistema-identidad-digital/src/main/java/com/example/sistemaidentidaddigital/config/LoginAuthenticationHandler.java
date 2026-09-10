package com.example.sistemaidentidaddigital.config;

import com.example.sistemaidentidaddigital.factory.Notificacion;
import com.example.sistemaidentidaddigital.factory.NotificacionFactory;
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

        // 1. Logeo Exitoso Singleton 
        loginManager.registrarInicioSesion(authentication.getName());

        // 2. Factory Method crea la notificación de Éxito
        Notificacion alerta = NotificacionFactory.crearNotificacion("EXITO");
        request.getSession().setAttribute("notificacionFlotante", alerta);

        response.sendRedirect("/");
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            org.springframework.security.core.AuthenticationException exception)
            throws IOException, ServletException {

        String email = request.getParameter("username");

        // 1. Logeo Fallo Singleton 
        loginManager.registrarIntentoFallido(email);

        // 2. Factory Method crea la notificación de Fallo
        Notificacion alerta = NotificacionFactory.crearNotificacion("FALLO");
        request.getSession().setAttribute("notificacionFlotante", alerta);

        response.sendRedirect("/login?error=true");
    }
}