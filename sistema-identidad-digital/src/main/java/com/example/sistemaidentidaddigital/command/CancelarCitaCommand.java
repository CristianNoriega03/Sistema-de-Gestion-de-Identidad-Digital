package com.example.sistemaidentidaddigital.command;

import com.example.sistemaidentidaddigital.memento.CitaMemento;
import com.example.sistemaidentidaddigital.model.Cita;
import com.example.sistemaidentidaddigital.repository.CitaRepository;

public class CancelarCitaCommand implements Command {

    private final Cita cita;
    private final CitaRepository citaRepository;
    
    // EL PATRÓN MEMENTO EN ACCIÓN: Ahora guardamos una foto completa, no un texto
    private CitaMemento memento;

    public CancelarCitaCommand(Cita cita, CitaRepository citaRepository) {
        this.cita = cita;
        this.citaRepository = citaRepository;
    }

    @Override
    public void ejecutar() {
        // 1. Le decimos a la cita que tome una fotografía ANTES de tocarla
        this.memento = cita.guardarEstado(); 
        
        // 2. Ejecutamos la acción de cancelar
        cita.setEstado("CANCELADA");
        citaRepository.save(cita);
        System.out.println("Comando Ejecutado: Cita " + cita.getId() + " cancelada exitosamente. Estado guardado en Memento.");
    }

    @Override
    public void deshacer() {
        // 3. Si nos piden deshacer, le pasamos la fotografía (Memento) a la cita para que se restaure
        if (this.memento != null) {
            cita.restaurarEstado(this.memento);
            citaRepository.save(cita);
            System.out.println("Comando Deshecho: Cita " + cita.getId() + " restaurada exitosamente desde el Memento.");
        }
    }
}