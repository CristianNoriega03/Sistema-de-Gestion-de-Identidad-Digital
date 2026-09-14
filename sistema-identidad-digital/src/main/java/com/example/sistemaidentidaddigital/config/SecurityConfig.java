package com.example.sistemaidentidaddigital.config;

import com.example.sistemaidentidaddigital.abstractfactory.LoginAbstractFactory;
import com.example.sistemaidentidaddigital.abstractfactory.RegistroAuditoria;
import com.example.sistemaidentidaddigital.factory.Notificacion;
import com.example.sistemaidentidaddigital.factory.NotificacionFactory;
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
                .logoutSuccessHandler((request, response, authentication) -> {
                    
                    // Obtenemos el email si existe, sino lo dejamos como "Desconocido"
                    String email = (authentication != null && authentication.getName() != null) 
                                    ? authentication.getName() 
                                    : "Desconocido";

                    // 1. Factory Method decide la fábrica abstracta
                    LoginAbstractFactory fabrica = NotificacionFactory.obtenerFamilia("LOGOUT");

                    // 2. Abstract Factory genera en paralelo la UI y la Auditoría
                    RegistroAuditoria logAuditoria = fabrica.crearAuditoria(email);
                    Notificacion alertaUI = fabrica.crearNotificacionUI();

                    // 3. Imprimimos el log y mandamos la notificación azul a la vista
                    System.out.println(logAuditoria.generarLog());
                    request.getSession().setAttribute("notificacionFlotante", alertaUI);
                    
                    response.sendRedirect("/login");
                })
                .permitAll()
            );

        return http.build();
    }
}