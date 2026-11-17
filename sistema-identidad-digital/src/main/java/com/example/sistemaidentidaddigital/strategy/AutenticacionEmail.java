package com.example.sistemaidentidaddigital.strategy;

public class AutenticacionEmail implements Estrategia2FA {
    @Override
    public void enviarCodigo(String destino, String codigo) {
        System.out.println("\n=====================================================");
        System.out.println("📧 [Strategy - 2FA] SIMULACIÓN DE EMAIL");
        System.out.println("➡️ Enviando correo electrónico a: " + destino);
        System.out.println("✉️ Tu código de acceso a la Registraduría es: [" + codigo + "]");
        System.out.println("=====================================================\n");
    }
}