package com.example.sistemaidentidaddigital.command;

import com.example.sistemaidentidaddigital.model.Cita;
import com.example.sistemaidentidaddigital.repository.CitaRepository;

public class CancelarCitaCommand implements Command {

    private final Cita cita;
    private final CitaRepository citaRepository;
    private String estadoAnterior; // Guardamos el estado anterior por si hay que deshacer

    public CancelarCitaCommand(Cita cita, CitaRepository citaRepository) {
        this.cita = cita;
        this.citaRepository = citaRepository;
    }

    @Override
    public void ejecutar() {
        // 1. Guardamos cómo estaba la cita antes de tocarla
        this.estadoAnterior = cita.getEstado(); 
        
        // 2. Ejecutamos la acción de cancelar
        cita.setEstado("CANCELADA");
        citaRepository.save(cita);
        System.out.println("Comando Ejecutado: Cita " + cita.getId() + " cancelada exitosamente.");
    }

    @Override
    public void deshacer() {
        // Si nos piden deshacer, restauramos el estado anterior
        if (estadoAnterior != null) {
            cita.setEstado(estadoAnterior);
            citaRepository.save(cita);
            System.out.println("Comando Deshecho: Cita " + cita.getId() + " restaurada a estado: " + estadoAnterior);
        }
    }
}