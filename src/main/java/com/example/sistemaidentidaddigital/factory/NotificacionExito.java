package com.example.sistemaidentidaddigital.factory;

public class NotificacionExito implements Notificacion {
    @Override
    public String getTipo() {
        return "success";
    }

    @Override
    public String getMensaje() {
        return "¡Inicio de sesión exitoso! Bienvenido!.";
    }
}