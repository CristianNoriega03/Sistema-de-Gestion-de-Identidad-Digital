package com.example.sistemaidentidaddigital.observer;

import com.example.sistemaidentidaddigital.model.Cita;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

// --- PATRÓN OBSERVER ---
// Este es el "Sujeto Principal". Tiene la lista de chismosos y les avisa a todos de golpe.
@Component
public class GestorNotificaciones {
    
    private List<ObservadorCita> observadores = new ArrayList<>();

    public GestorNotificaciones() {
        // Suscribimos a los chismosos automáticamente al arrancar
        this.observadores.add(new EmailObserver());
        this.observadores.add(new AuditoriaObserver());
    }

    // Método que grita a los cuatro vientos que algo cambió
    public void notificarCambio(Cita cita, String accion) {
        System.out.println("[Patrón Observer] 📢 Atención observadores, la cita " + cita.getId() + " cambió.");
        for (ObservadorCita obs : observadores) {
            obs.actualizar(cita, accion);
        }
    }
}