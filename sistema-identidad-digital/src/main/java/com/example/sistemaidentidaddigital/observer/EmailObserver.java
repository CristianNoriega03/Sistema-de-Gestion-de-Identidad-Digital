package com.example.sistemaidentidaddigital.observer;

import com.example.sistemaidentidaddigital.model.Cita;

// --- PATRÓN OBSERVER ---
// Observador 1: Se entera del cambio y simula enviar un correo.
public class EmailObserver implements ObservadorCita {
    @Override
    public void actualizar(Cita cita, String accion) {
        System.out.println("[Patrón Observer - Email] 📧 Simulando envío de correo a: " 
            + cita.getCiudadano().getEmail() + " | Motivo: La cita fue " + accion);
    }
}