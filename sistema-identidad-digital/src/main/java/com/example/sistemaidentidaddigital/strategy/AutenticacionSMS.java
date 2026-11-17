package com.example.sistemaidentidaddigital.strategy;

public class AutenticacionSMS implements Estrategia2FA {
    @Override
    public void enviarCodigo(String destino, String codigo) {
        System.out.println("\n=====================================================");
        System.out.println("📱 [Strategy - 2FA] SIMULACIÓN DE SMS");
        System.out.println("➡️ Enviando mensaje de texto al número: " + destino);
        System.out.println("💬 Tu código de acceso a la Registraduría es: [" + codigo + "]");
        System.out.println("=====================================================\n");
    }
}