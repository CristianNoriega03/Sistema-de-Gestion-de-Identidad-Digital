package com.example.sistemaidentidaddigital.visitor;

import com.example.sistemaidentidaddigital.model.Cita;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// --- PATRÓN VISITOR ---
// Visitante Concreto: Visita la cita atendida y exporta un reporte formal dentro de la carpeta 'comprobantes'.
public class ReporteAuditoriaHtmlVisitor implements VisitorCita {

    private String rutaArchivoHtml;

    @Override
    public void visitar(Cita cita) {
        System.out.println("[Patrón Visitor] 📄 Visitando Cita ID: " + cita.getId() + " para generar reporte oficial...");

        // 1. Definir y asegurar que la carpeta "comprobantes" exista
        String directorioComprobantes = "comprobantes";
        File carpeta = new File(directorioComprobantes);
        if (!carpeta.exists()) {
            carpeta.mkdirs(); // Crea la carpeta automáticamente si no existe
        }

        // 2. Generar nombre de archivo único con fecha y hora
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String nombreArchivo = "Comprobante_Cita_" + cita.getId() + "_" + timestamp + ".html";
        this.rutaArchivoHtml = directorioComprobantes + File.separator + nombreArchivo;

        // 3. Estructura HTML del documento formal de la Registraduría
        String htmlContent = "<!-- COMPROBANTE OFICIAL - PATRÓN VISITOR -->\n" +
            "<!DOCTYPE html>\n" +
            "<html lang=\"es\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Comprobante de Atención - Registraduría</title>\n" +
            "    <style>\n" +
            "        body { font-family: Arial, sans-serif; margin: 40px; background-color: #f4f6f8; color: #333; }\n" +
            "        .card { max-width: 600px; background: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); margin: auto; }\n" +
            "        .header { text-align: center; border-bottom: 2px solid #0056b3; padding-bottom: 15px; margin-bottom: 20px; }\n" +
            "        .header h2 { margin: 0; color: #0056b3; font-size: 20px; }\n" +
            "        .header p { margin: 5px 0 0; color: #666; font-size: 14px; }\n" +
            "        .section-title { font-weight: bold; color: #333; margin-top: 20px; border-bottom: 1px solid #ddd; padding-bottom: 5px; }\n" +
            "        .details p { margin: 8px 0; font-size: 15px; }\n" +
            "        .footer { text-align: center; margin-top: 30px; font-size: 12px; color: #888; border-top: 1px solid #eee; padding-top: 15px; }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"card\">\n" +
            "        <div class=\"header\">\n" +
            "            <h2>REGISTRADURÍA NACIONAL DEL ESTADO CIVIL</h2>\n" +
            "            <p>Sistema de Identidad Digital - Comprobante de Atención</p>\n" +
            "        </div>\n" +
            "        <div class=\"details\">\n" +
            "            <div class=\"section-title\">DETALLES DEL TRÁMITE</div>\n" +
            "            <p><strong>• ID de Ticket / Cita:</strong> #" + cita.getId() + "</p>\n" +
            "            <p><strong>• Fecha Programada:</strong> " + cita.getFecha() + " a las " + cita.getHora() + "</p>\n" +
            "            <p><strong>• Estado del Trámite:</strong> <span style=\"color: #28a745; font-weight: bold;\">" + cita.getEstado() + "</span></p>\n" +
            "            \n" +
            "            <div class=\"section-title\">DATOS DEL CIUDADANO</div>\n" +
            "            " + (cita.getCiudadano() != null ? 
                "<p><strong>• Nombre Completo:</strong> " + cita.getCiudadano().getNombre() + " " + cita.getCiudadano().getApellido() + "</p>\n" +
                "<p><strong>• Documento de Identidad:</strong> " + cita.getCiudadano().getDocumento() + "</p>\n" +
                "<p><strong>• Correo Electrónico:</strong> " + cita.getCiudadano().getEmail() + "</p>" : 
                "<p>Información de ciudadano no disponible.</p>") + "\n" +
            "        </div>\n" +
            "        <div class=\"footer\">\n" +
            "            Reporte oficial generado automáticamente mediante el Patrón Visitor.<br>\n" +
            "            Arquitectura de Software - Sistema de Identidad Digital\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";

        // 4. Guardar el archivo dentro de la carpeta creada
        try (FileWriter fileWriter = new FileWriter(this.rutaArchivoHtml)) {
            fileWriter.write(htmlContent);
            System.out.println("[Patrón Visitor] ✅ ¡Comprobante guardado en la carpeta 'comprobantes/'! Archivo: " + this.rutaArchivoHtml);
        } catch (IOException e) {
            System.err.println("[Patrón Visitor] ❌ Error al guardar el archivo: " + e.getMessage());
        }
    }

    public String getRutaArchivoHtml() {
        return rutaArchivoHtml;
    }
}