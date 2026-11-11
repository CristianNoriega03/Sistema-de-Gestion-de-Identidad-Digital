package com.example.sistemaidentidaddigital.command;

import com.example.sistemaidentidaddigital.memento.CitaMemento;
import com.example.sistemaidentidaddigital.model.Cita;
import com.example.sistemaidentidaddigital.repository.CitaRepository;

public class CancelarCitaCommand implements Command {

    private final Cita cita;
    private final CitaRepository citaRepository;
    
    // --- APLICACIÓN DEL PATRÓN MEMENTO ---
    private CitaMemento memento;

    public CancelarCitaCommand(Cita cita, CitaRepository citaRepository) {
        this.cita = cita;
        this.citaRepository = citaRepository;
    }

    @Override
    public void ejecutar() {
        // 1. (Memento) Toma una fotografía ANTES de tocar la cita
        this.memento = cita.guardarEstado(); 
        
        // --- APLICACIÓN DEL PATRÓN STATE ---
        // 2. Ejecutamos la acción delegando la lógica al estado actual (aplica candados)
        cita.procesarCancelacion(); 
        
        citaRepository.save(cita);
        System.out.println("Comando Ejecutado: Cita " + cita.getId() + " cancelada exitosamente. Estado guardado en Memento.");
    }

    @Override
    public void deshacer() {
        // 3. (Memento) Restaura la cita leyendo la cápsula del pasado
        if (this.memento != null) {
            cita.restaurarEstado(this.memento);
            citaRepository.save(cita);
            System.out.println("Comando Deshecho: Cita " + cita.getId() + " restaurada exitosamente desde el Memento.");
        }
    }
}