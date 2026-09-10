package com.example.sistemaidentidaddigital.service;

import com.example.sistemaidentidaddigital.model.Ciudadano;
import com.example.sistemaidentidaddigital.repository.CiudadanoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CiudadanoService {

    private final CiudadanoRepository ciudadanoRepository;
    private final PasswordEncoder passwordEncoder;

    public CiudadanoService(
            CiudadanoRepository ciudadanoRepository,
            PasswordEncoder passwordEncoder) {

        this.ciudadanoRepository = ciudadanoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registrarCiudadano(
            String nombre,
            String apellido,
            String documento,
            String fechaNacimiento,
            String telefono,
            String email,
            String password,
            String confirmarPassword) {

        if (!password.equals(confirmarPassword)) {
            return "Las contraseñas no coinciden.";
        }

        if (ciudadanoRepository.existsByEmail(email)) {
            return "El correo ya está registrado.";
        }

        if (ciudadanoRepository.existsByDocumento(documento)) {
            return "El documento ya está registrado.";
        }

        Ciudadano ciudadano = new Ciudadano();

        ciudadano.setNombre(nombre);
        ciudadano.setApellido(apellido);
        ciudadano.setDocumento(documento);
        ciudadano.setFechaNacimiento(fechaNacimiento);
        ciudadano.setTelefono(telefono);
        ciudadano.setEmail(email);

        ciudadano.setPassword(
                passwordEncoder.encode(password)
        );

        ciudadanoRepository.save(ciudadano);

        return null;
    }
}