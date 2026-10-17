package com.example.sistemaidentidaddigital;

import com.example.sistemaidentidaddigital.builder.CiudadanoBuilder;
import com.example.sistemaidentidaddigital.model.Ciudadano;
import com.example.sistemaidentidaddigital.repository.CiudadanoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SistemaIdentidadDigitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaIdentidadDigitalApplication.class, args);
    }

    // Este código se ejecuta automáticamente cada vez que se ejecuta el proyecto
    @Bean
    public CommandLineRunner inicializarSuperAdmin(CiudadanoRepository repositorio, PasswordEncoder passwordEncoder) {
        return args -> {
            String emailSuperAdmin = "superadmin@registraduria.gov.co";
            
            if (!repositorio.existsByEmail(emailSuperAdmin)) {
                // Usamos TU patrón Builder para crear al jefe
                Ciudadano superAdmin = new CiudadanoBuilder()
                        .conNombre("Jefe")
                        .conApellido("Sistemas")
                        .conDocumento("0000000000") // Cédula simulada
                        .conEmail(emailSuperAdmin)
                        .conPassword(passwordEncoder.encode("admin123")) // Contraseña: admin123
                        .conRol("SUPERADMIN") // ¡Le damos el rol máximo!
                        .build();

                repositorio.save(superAdmin);
                System.out.println("👑 ATENCIÓN: Super Administrador creado con éxito en la base de datos.");
            }
        };
    }
}