package com.example.sistemaidentidaddigital.observer;

import com.example.sistemaidentidaddigital.model.Cita;

// --- PATRÓN OBSERVER ---
// Interfaz base: Todos los "chismosos" deben implementar esto para recibir la alerta.
public interface ObservadorCita {
    void actualizar(Cita cita, String accion);
}