package org.example.proyectopp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Representa el conteo de un error específico en los logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorCount {
     @Schema(description = "Código HTTP del error o código interno definido")
    private int code;
      @Schema(description = "Cantidad de veces que ocurrió este error en los logs")
    private long count;
}