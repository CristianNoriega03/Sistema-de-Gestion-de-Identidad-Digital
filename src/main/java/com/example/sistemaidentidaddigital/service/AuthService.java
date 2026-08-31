package com.example.sistemaidentidaddigital.service;

import com.example.sistemaidentidaddigital.model.Ciudadano;
import com.example.sistemaidentidaddigital.repository.CiudadanoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final CiudadanoRepository ciudadanoRepository;

    public AuthService(CiudadanoRepository ciudadanoRepository) {
        this.ciudadanoRepository = ciudadanoRepository;
    }

    public Optional<Ciudadano> buscarPorEmail(String email) {
        return ciudadanoRepository.findByEmail(email);
    }
}
