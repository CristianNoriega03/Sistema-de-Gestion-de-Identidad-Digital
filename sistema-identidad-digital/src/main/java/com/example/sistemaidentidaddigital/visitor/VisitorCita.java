package com.example.sistemaidentidaddigital.visitor;

import com.example.sistemaidentidaddigital.model.Cita;

// --- PATRÓN VISITOR ---
// Interfaz base: Define qué operaciones puede realizar el visitante sobre una Cita.
public interface VisitorCita {
    void visitar(Cita cita);
}