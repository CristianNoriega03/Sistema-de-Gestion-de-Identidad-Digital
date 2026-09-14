package com.example.sistemaidentidaddigital.abstractfactory;

import com.example.sistemaidentidaddigital.factory.Notificacion;
import com.example.sistemaidentidaddigital.factory.NotificacionExito;

public class ExitoFactory implements LoginAbstractFactory {
    @Override
    public Notificacion crearNotificacionUI() {
        return new NotificacionExito(); 
    }

    @Override
    public RegistroAuditoria crearAuditoria(String email) {
        return new AuditoriaExito(email);
    }
}