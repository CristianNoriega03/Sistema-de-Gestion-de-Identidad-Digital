package com.example.sistemaidentidaddigital.strategy;

public class ContextoAutenticacion2FA {
    
    private Estrategia2FA estrategia;

    public void setEstrategia(Estrategia2FA estrategia) {
        this.estrategia = estrategia;
    }

    public void ejecutarEnvio(String destino, String codigo) {
        if (this.estrategia == null) {
            throw new IllegalStateException("No se ha seleccionado una estrategia de autenticación.");
        }
        this.estrategia.enviarCodigo(destino, codigo);
    }
}