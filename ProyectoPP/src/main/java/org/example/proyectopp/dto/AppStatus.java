package org.example.proyectopp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resumen del estado actual de la aplicación")
public class AppStatus {
        @Schema(description = "Número total de solicitudes procesadas")
    private long totalRequests;
        @Schema(description = "Número total de errores registrados")
    private long totalErrors;
        @Schema(description = "Tiempo promedio de respuesta en milisegundos")
    private double avgResponseMs;
}