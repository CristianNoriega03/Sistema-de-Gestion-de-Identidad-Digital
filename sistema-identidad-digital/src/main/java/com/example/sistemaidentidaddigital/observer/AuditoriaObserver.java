package com.example.sistemaidentidaddigital.observer;

import com.example.sistemaidentidaddigital.model.Cita;

// --- PATRÓN OBSERVER ---
// Observador 2: Se entera del cambio y deja un registro de seguridad.
public class AuditoriaObserver implements ObservadorCita {
    @Override
    public void actualizar(Cita cita, String accion) {
        System.out.println("[Patrón Observer - Auditoría] 🔐 Guardando en log de seguridad. Trámite ID: " 
            + cita.getId() + " pasó a estado: " + accion);
    }
}