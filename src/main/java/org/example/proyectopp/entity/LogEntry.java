package org.example.proyectopp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "log_entries")
public class LogEntry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant timestamp;       // cuándo ocurrió
    private String level;            // INFO/WARN/ERROR
    private String httpMethod;       // GET/POST/PUT/DELETE
    private String endpoint;         // URI del request

    private int status;              // 2xx/4xx/5xx
    private long durationMs;         // ms medidos por el filtro

    private String exceptionName;    // si hubo excepción
    private String message;          // opcional
}
