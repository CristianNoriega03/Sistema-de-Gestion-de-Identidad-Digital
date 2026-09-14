package com.example.sistemaidentidaddigital.abstractfactory;

import com.example.sistemaidentidaddigital.factory.Notificacion;
import com.example.sistemaidentidaddigital.factory.NotificacionFallo;

public class FalloFactory implements LoginAbstractFactory {
    @Override
    public Notificacion crearNotificacionUI() {
        return new NotificacionFallo(); 
    }

    @Override
    public RegistroAuditoria crearAuditoria(String email) {
        return new AuditoriaFallo(email);
    }
}