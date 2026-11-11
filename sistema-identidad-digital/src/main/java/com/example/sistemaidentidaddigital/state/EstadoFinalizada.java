package com.example.sistemaidentidaddigital.state;

import com.example.sistemaidentidaddigital.model.Cita;

// Este estado representa una cita que el Admin ya atendió (el trámite se completó).
public class EstadoFinalizada implements EstadoCita {

    @Override
    public void cancelar(Cita cita) {
        // CANDADO PRINCIPAL: Protege la base de datos si intentan forzar la URL de cancelación.
        System.out.println("[Patrón State] Acción denegada: Transición inválida de FINALIZADA a CANCELADA.");
        throw new IllegalStateException("Error de seguridad: La cita ya fue atendida, no se puede cancelar.");
    }

    @Override
    public void finalizar(Cita cita) {
        System.out.println("[Patrón State] La cita ya se encuentra FINALIZADA.");
    }
}