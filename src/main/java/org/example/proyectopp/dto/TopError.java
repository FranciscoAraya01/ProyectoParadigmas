package org.example.proyectopp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error con mayor frecuencia detectado en los logs")
public class TopError {
      @Schema(description = "Nombre del error o código HTTP")
    private String error;
     @Schema(description = "Número de veces que se repitió")
    private long count;
}