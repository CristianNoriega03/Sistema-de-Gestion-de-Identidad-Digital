package com.example.sistemaidentidaddigital.abstractfactory;

import com.example.sistemaidentidaddigital.factory.Notificacion;

public interface LoginAbstractFactory {
    Notificacion crearNotificacionUI();
    RegistroAuditoria crearAuditoria(String email);
}