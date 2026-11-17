package com.example.sistemaidentidaddigital.template;

import com.example.sistemaidentidaddigital.facade.RespuestaFacade;
import com.example.sistemaidentidaddigital.facade.TramiteCitasFacade;

public class CitaEstandarRegistraduria extends GestorCitaTemplate {

    // Constructor que hereda y pasa la Facade al padre
    public CitaEstandarRegistraduria(TramiteCitasFacade tramiteCitasFacade) {
        super(tramiteCitasFacade);
    }

    @Override
    protected RespuestaFacade ejecutarAgendamiento(String email, String fecha, String hora, boolean confirmarDenuncio, boolean pagoAprobado) {
        System.out.println("2. [Template] Ejecutando agendamiento estándar mediante Facade.");
        return tramiteCitasFacade.agendarNuevaCita(email, fecha, hora, confirmarDenuncio, pagoAprobado);
    }
}