package com.example.sistemaidentidaddigital.factory;

public class NotificacionInfo implements Notificacion {
    @Override
    public String getTipo() {
        return "info"; // Color azul
    }

    @Override
    public String getMensaje() {
        return "Has cerrado sesión de forma segura.";
    }
}