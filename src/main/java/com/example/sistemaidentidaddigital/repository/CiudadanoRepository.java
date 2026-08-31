package com.example.sistemaidentidaddigital.repository;

import com.example.sistemaidentidaddigital.model.Ciudadano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CiudadanoRepository extends JpaRepository<Ciudadano, Long> {

    Optional<Ciudadano> findByEmail(String email);

    boolean existsByDocumento(String documento);

    boolean existsByEmail(String email);
}