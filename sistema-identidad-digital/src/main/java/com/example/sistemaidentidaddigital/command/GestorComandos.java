package com.example.sistemaidentidaddigital.command;

import org.springframework.stereotype.Component;
import java.util.Stack;

@Component
public class GestorComandos {
    
    // Usamos una pila (Stack) para guardar el historial de comandos ejecutados
    private final Stack<Command> historialComandos = new Stack<>();

    public void ejecutarComando(Command comando) {
        comando.ejecutar();
        historialComandos.push(comando); // Guardamos el comando en el historial
    }

    // AHORA DEVUELVE BOOLEAN EN LUGAR DE VOID
    public boolean deshacerUltimoComando() {
        if (!historialComandos.isEmpty()) {
            Command ultimoComando = historialComandos.pop();
            ultimoComando.deshacer();
            return true;
        } else {
            System.out.println("No hay acciones para deshacer.");
            return false;
        }
    }
    
    // Método para saber si hay algo que deshacer (para mostrar o no el botón)
    public boolean hayComandosParaDeshacer() {
        return !historialComandos.isEmpty();
    }
}