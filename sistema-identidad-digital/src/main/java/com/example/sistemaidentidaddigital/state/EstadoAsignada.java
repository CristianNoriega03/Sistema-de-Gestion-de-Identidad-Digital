package com.example.sistemaidentidaddigital.state;

import com.example.sistemaidentidaddigital.model.Cita;

// Este estado representa una cita normal, recién agendada.
public class EstadoAsignada implements EstadoCita {

    @Override
    public void cancelar(Cita cita) {
        // Como está asignada, SÍ se permite cancelar sin problema.
        System.out.println("[Patrón State] Transición permitida: Cambiando estado de ASIGNADA a CANCELADA.");
        cita.setEstado("CANCELADA"); // Cambia el String para guardarlo en la Base de Datos
        cita.setEstadoComportamiento(new EstadoCancelada()); // Pasa el comportamiento al nuevo estado
    }

    @Override
    public void finalizar(Cita cita) {
        // Como está asignada, SÍ se permite que el Admin la atienda.
        System.out.println("[Patrón State] Transición permitida: Cambiando estado de ASIGNADA a FINALIZADA.");
        cita.setEstado("FINALIZADA"); 
        cita.setEstadoComportamiento(new EstadoFinalizada());
    }
}