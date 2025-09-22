package org.example.proyectopp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estadísticas de tiempos de respuesta de la aplicación")
public class ResponseStats {
     @Schema(description = "Tiempo promedio de respuesta (ms)")
    private double avg;
        @Schema(description = "Tiempo mínimo de respuesta (ms)")
    private long min;
        @Schema(description = "Tiempo máximo de respuesta (ms)")
    private long max;
        @Schema(description = "Mediana de los tiempos de respuesta (ms)")
    private double median;
}