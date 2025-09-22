package org.example.proyectopp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representa el uso de un endpoint específico en los logs")
public class EndpointUsage {
        @Schema(description = "Ruta del endpoint")
    private String endpoint;
        @Schema(description = "Número total de veces que se accedió a este endpoint")
    private long total;
}
