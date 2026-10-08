package com.example.sistemaidentidaddigital.facade;

import com.example.sistemaidentidaddigital.composite.PaqueteRequisitos;
import com.example.sistemaidentidaddigital.composite.RequisitoDenuncio;
import com.example.sistemaidentidaddigital.composite.RequisitoPago;
import com.example.sistemaidentidaddigital.model.Cita;
import com.example.sistemaidentidaddigital.model.Ciudadano;
import com.example.sistemaidentidaddigital.repository.CitaRepository;
import com.example.sistemaidentidaddigital.repository.CiudadanoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TramiteCitasFacade {

    private final CitaRepository citaRepository;
    private final CiudadanoRepository ciudadanoRepository;

    // La fachada esconde la complejidad de las bases de datos
    public TramiteCitasFacade(CitaRepository citaRepository, CiudadanoRepository ciudadanoRepository) {
        this.citaRepository = citaRepository;
        this.ciudadanoRepository = ciudadanoRepository;
    }

    // El método principal que orquesta todo el proceso
    public RespuestaFacade agendarNuevaCita(String email, String fecha, String hora, boolean confirmarDenuncio, boolean pagoAprobado) {
        
        // 1. BUSCAR AL CIUDADANO
        Ciudadano ciudadano = ciudadanoRepository.findByEmail(email).orElse(null);
        if (ciudadano == null) {
            return new RespuestaFacade(false, "❌ Usuario no encontrado en el sistema.");
        }

        // 2. BARRERA DE SEGURIDAD (Evitar doble cita)
        List<Cita> citasExistentes = citaRepository.findByCiudadano(ciudadano);
        boolean yaTieneCita = citasExistentes.stream().anyMatch(c -> c.getEstado().equals("ASIGNADA"));
        if (yaTieneCita) {
            return new RespuestaFacade(false, "❌ No puedes sacar otra cita porque ya tienes un trámite activo.");
        }

        // 3. APLICACIÓN DEL PATRÓN COMPOSITE (Validar requisitos)
        // el Composite sigue funcionando intacto, pero ahora escondido en la Fachada
        PaqueteRequisitos paqueteRenovacion = new PaqueteRequisitos();
        paqueteRenovacion.agregarRequisito(new RequisitoDenuncio(confirmarDenuncio));
        paqueteRenovacion.agregarRequisito(new RequisitoPago(pagoAprobado));

        if (!paqueteRenovacion.validar()) {
            return new RespuestaFacade(false, "❌ Faltan requisitos. Asegúrese de marcar el denuncio y realizar el pago.");
        }

        // 4. GUARDAR LA CITA SI TODO PASÓ
        Cita nuevaCita = new Cita();
        nuevaCita.setFecha(fecha);
        nuevaCita.setHora(hora);
        nuevaCita.setEstado("ASIGNADA");
        nuevaCita.setCiudadano(ciudadano);
        citaRepository.save(nuevaCita);

        return new RespuestaFacade(true, "📅 Cita asignada correctamente.");
    }
}