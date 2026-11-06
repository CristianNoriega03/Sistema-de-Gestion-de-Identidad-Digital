package com.example.sistemaidentidaddigital.mediator;

import com.example.sistemaidentidaddigital.abstractfactory.LoginAbstractFactory;
import com.example.sistemaidentidaddigital.abstractfactory.RegistroAuditoria;
import com.example.sistemaidentidaddigital.factory.Notificacion;
import com.example.sistemaidentidaddigital.factory.NotificacionFactory;
import com.example.sistemaidentidaddigital.util.LoginManager;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class MediadorSeguridad implements Mediador {

    private final LoginManager loginManager;

    public MediadorSeguridad() {
        this.loginManager = LoginManager.getInstance();
    }

    @Override
    public void notificarEvento(String evento, String email, HttpSession session) {
        
        // 1. Habla con el Singleton
        if (evento.equals("EXITO")) {
            loginManager.registrarInicioSesion(email);
        } else if (evento.equals("FALLO")) {
            loginManager.registrarIntentoFallido(email);
        }

        // 2. Habla con el Abstract Factory
        LoginAbstractFactory fabrica = NotificacionFactory.obtenerFamilia(evento);
        if (fabrica != null) {
            RegistroAuditoria logAuditoria = fabrica.crearAuditoria(email);
            Notificacion alertaUI = fabrica.crearNotificacionUI();

            // 3. Orquesta las salidas (Consola y Vista)
            System.out.println(logAuditoria.generarLog());
            if (session != null) {
                session.setAttribute("notificacionFlotante", alertaUI);
            }
        }
    }
}