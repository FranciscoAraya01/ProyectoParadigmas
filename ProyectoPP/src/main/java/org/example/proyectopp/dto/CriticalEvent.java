package org.example.proyectopp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Evento crítico detectado en los logs")
public class CriticalEvent {
        @Schema(description = "Tipo de evento crítico")
    private String type;
        @Schema(description = "Detalle o mensaje del evento")
    private String detail;
        @Schema(description = "Marca de tiempo del evento")
    private Instant at;
}