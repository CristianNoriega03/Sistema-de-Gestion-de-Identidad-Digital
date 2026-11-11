package com.example.sistemaidentidaddigital.model;
import com.example.sistemaidentidaddigital.memento.CitaMemento;
import jakarta.persistence.*;
import com.example.sistemaidentidaddigital.visitor.VisitorCita;

import com.example.sistemaidentidaddigital.state.*;

@Entity
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fecha;
    private String hora;
    private String estado; // Aquí guardaremos "ASIGNADA", "FINALIZADA" o "CANCELADA"

    // Relación: Muchos trámites de cita pueden pertenecer a un solo ciudadano
    @ManyToOne
    @JoinColumn(name = "ciudadano_id")
    private Ciudadano ciudadano;

    // --- Getters y Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Ciudadano getCiudadano() { return ciudadano; }
    public void setCiudadano(Ciudadano ciudadano) { this.ciudadano = ciudadano; }


// --- APLICACIÓN DEL PATRÓN MEMENTO ---

    // 1. Crea una cápsula con su estado actual
    public CitaMemento guardarEstado() {
        return new CitaMemento(this.fecha, this.hora, this.estado);
    }

    // 2. Se restaura leyendo la cápsula del pasado
    public void restaurarEstado(CitaMemento memento) {
        this.fecha = memento.getFecha();
        this.hora = memento.getHora();
        this.estado = memento.getEstado();
    }


// --- APLICACIÓN DEL PATRÓN STATE ---

    // 1. Atributo temporal (ignorado por BD) que maneja el comportamiento del estado actual
    @Transient 
    private EstadoCita estadoComportamiento;

    public void setEstadoComportamiento(EstadoCita estadoComportamiento) {
        this.estadoComportamiento = estadoComportamiento;
    }

    // 2. Convierte el texto de la BD en un objeto de estado inteligente
    public void cargarEstadoComportamiento() {
        if (this.estado == null || this.estado.equals("ASIGNADA")) {
            this.estadoComportamiento = new EstadoAsignada();
        } else if (this.estado.equals("FINALIZADA")) {
            this.estadoComportamiento = new EstadoFinalizada();
        } else if (this.estado.equals("CANCELADA")) {
            this.estadoComportamiento = new EstadoCancelada();
        }
    }

    // 3. Delega la acción de cancelar al estado actual (aplica candados)
    public void procesarCancelacion() {
        if (this.estadoComportamiento == null) cargarEstadoComportamiento();
        this.estadoComportamiento.cancelar(this);
    }

    // 4. Delega la acción de finalizar al estado actual (aplica candados)
    public void procesarFinalizacion() {
        if (this.estadoComportamiento == null) cargarEstadoComportamiento();
        this.estadoComportamiento.finalizar(this);
    }


// --- PATRÓN VISITOR ---
public void aceptar(VisitorCita visitor) {
    visitor.visitar(this);
}

}