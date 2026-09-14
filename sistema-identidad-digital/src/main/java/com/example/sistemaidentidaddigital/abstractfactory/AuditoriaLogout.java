package com.example.sistemaidentidaddigital.abstractfactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditoriaLogout implements RegistroAuditoria {
    private String email;

    public AuditoriaLogout(String email) {
        this.email = email;
    }

    @Override
    public String generarLog() {
        String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        return "Auditoria Seguridad - Logout [" + fechaHora + "] usuario : " + email + " - Detalle: Cierre de sesion seguro.";
    }
}