package com.example.sistemaidentidaddigital.template;

import com.example.sistemaidentidaddigital.facade.RespuestaFacade;
import com.example.sistemaidentidaddigital.facade.TramiteCitasFacade;

public abstract class GestorCitaTemplate {

    protected TramiteCitasFacade tramiteCitasFacade;

    // Constructor que recibe la Facade
    public GestorCitaTemplate(TramiteCitasFacade tramiteCitasFacade) {
        this.tramiteCitasFacade = tramiteCitasFacade;
    }

    // EL MÉTODO PLANTILLA: Orden estricto del proceso
    public final RespuestaFacade procesarFlujoCita(String email, String fecha, String hora, boolean confirmarDenuncio, boolean pagoAprobado) {
        validarParametros(fecha, hora);
        RespuestaFacade respuesta = ejecutarAgendamiento(email, fecha, hora, confirmarDenuncio, pagoAprobado);
        postProcesamiento(respuesta);
        return respuesta;
    }

    // Paso 1: Común
    protected void validarParametros(String fecha, String hora) {
        System.out.println("1. [Template] Validando parámetros básicos de la cita.");
    }

    // PASO ABSTRACTO: Obligatorio que las hijas lo implementen
    protected abstract RespuestaFacade ejecutarAgendamiento(String email, String fecha, String hora, boolean confirmarDenuncio, boolean pagoAprobado);

    // Paso 3: Común
    protected void postProcesamiento(RespuestaFacade respuesta) {
        System.out.println("3. [Template] Proceso de cita finalizado.");
    }
}