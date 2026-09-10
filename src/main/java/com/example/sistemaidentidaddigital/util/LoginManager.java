package com.example.sistemaidentidaddigital.util;

public class LoginManager {

     // Guarda la única instancia de LoginManager
    private static LoginManager instancia;

    // evitar crear objetos desde otras clases
    private LoginManager() {
         System.out.println("---- LoginManager instanciado UNA SOLA VEZ ----");
    }

    // Devuelve la única instancia del Singleton
    public static LoginManager getInstance() {

        //Si todavia no existe se crear
        if (instancia == null) {
            instancia = new LoginManager();
        }

        //Devuelve la instancia existente
        return instancia;
    }

    // Registra un inicio de sesión exitoso
    public void registrarInicioSesion(String email) {
        System.out.println("Inicio de sesión exitoso: " + email);
    }

    // Registra un inicio de sesión fallido
    public void registrarIntentoFallido(String email) {
        System.out.println("Intento de inicio de sesión fallido: " + email);
    }
}

