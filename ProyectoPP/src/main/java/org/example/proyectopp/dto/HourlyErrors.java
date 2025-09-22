package org.example.proyectopp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Cantidad de errores registrados en un intervalo horario específico")
public class HourlyErrors {
        @Schema(description = "Hora del día en formato 24 horas (0-23)")
    private int hour;
        @Schema(description = "Número total de errores registrados en esa hora")
    private long count;
}