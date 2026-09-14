package com.example.sistemaidentidaddigital.abstractfactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditoriaFallo implements RegistroAuditoria {
    private String email;

    public AuditoriaFallo(String email) {
        this.email = email;
    }

    @Override
    public String generarLog() {
        String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        return "Auditoria Seguridad - Fallo [" + fechaHora + "] usuario : " + email + " - Detalle: Credenciales incorrectas.";
    }
}