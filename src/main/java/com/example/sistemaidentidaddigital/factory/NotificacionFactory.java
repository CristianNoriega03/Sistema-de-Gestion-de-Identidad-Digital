package com.example.sistemaidentidaddigital.factory;

public class NotificacionFactory {

    public static Notificacion crearNotificacion(String tipo) {
        if (tipo == null) return null;

        switch (tipo.toUpperCase()) {
            case "EXITO":
                return new NotificacionExito();
            case "FALLO":
                return new NotificacionFallo();
            case "LOGOUT":
            case "INFO":
                return new NotificacionInfo();
            default:
                return null;
        }
    }
}