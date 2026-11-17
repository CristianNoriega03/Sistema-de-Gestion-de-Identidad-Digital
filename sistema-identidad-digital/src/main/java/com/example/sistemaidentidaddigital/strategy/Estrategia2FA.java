package com.example.sistemaidentidaddigital.strategy;

public interface Estrategia2FA {
    void enviarCodigo(String destino, String codigo);
}