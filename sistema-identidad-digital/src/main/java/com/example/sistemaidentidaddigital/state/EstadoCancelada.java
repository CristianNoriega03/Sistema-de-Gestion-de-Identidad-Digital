package com.example.sistemaidentidaddigital.state;

import com.example.sistemaidentidaddigital.model.Cita;

// Este estado representa una cita que ya fue cancelada por el usuario o el admin.
public class EstadoCancelada implements EstadoCita {

    @Override
    public void cancelar(Cita cita) {
        System.out.println("[Patrón State] La cita ya se encuentra CANCELADA.");
    }

    @Override
    public void finalizar(Cita cita) {
        // CANDADO: Bloquea intentos de atender (finalizar) una cita que fue cancelada.
        System.out.println("[Patrón State] Acción denegada: Transición inválida de CANCELADA a FINALIZADA.");
        throw new IllegalStateException("Error de seguridad: No se puede atender una cita que ya fue cancelada.");
    }
}