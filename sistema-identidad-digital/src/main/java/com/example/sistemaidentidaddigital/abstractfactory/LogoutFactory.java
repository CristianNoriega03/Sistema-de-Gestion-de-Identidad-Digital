package com.example.sistemaidentidaddigital.abstractfactory;

import com.example.sistemaidentidaddigital.factory.Notificacion;
import com.example.sistemaidentidaddigital.factory.NotificacionInfo;

public class LogoutFactory implements LoginAbstractFactory {
    @Override
    public Notificacion crearNotificacionUI() {
        return new NotificacionInfo(); 
    }

    @Override
    public RegistroAuditoria crearAuditoria(String email) {
        return new AuditoriaLogout(email);
    }
}