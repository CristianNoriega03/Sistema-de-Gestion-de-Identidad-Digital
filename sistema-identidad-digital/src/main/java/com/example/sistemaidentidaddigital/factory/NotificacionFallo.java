package com.example.sistemaidentidaddigital.factory;

public class NotificacionFallo implements Notificacion {
    @Override
    public String getTipo() {
        return "warning"; // Color Naranja
    }

    @Override
    public String getMensaje() {
        return "Intento de acceso fallido. Revisa tus datos nuevamente.";
    }
}