package com.example.sistemaidentidaddigital.facade;

// Un objeto sencillo para transportar la respuesta desde la Fachada hasta el Controlador
public class RespuestaFacade {
    private boolean exito;
    private String mensaje;

    public RespuestaFacade(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    public boolean isExito() {
        return exito;
    }

    public String getMensaje() {
        return mensaje;
    }
}