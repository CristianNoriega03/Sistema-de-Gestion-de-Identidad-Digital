package com.example.sistemaidentidaddigital.repository;

import com.example.sistemaidentidaddigital.model.Cita;
import com.example.sistemaidentidaddigital.model.Ciudadano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    
    // Spring Boot crea la consulta SQL automáticamente con este nombre
    List<Cita> findByCiudadano(Ciudadano ciudadano);
    
}