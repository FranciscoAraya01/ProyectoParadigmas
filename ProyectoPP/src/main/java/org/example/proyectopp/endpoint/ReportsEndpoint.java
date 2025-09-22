package org.example.proyectopp.endpoint;

//swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;


import lombok.RequiredArgsConstructor;
import org.example.proyectopp.dto.*;
import org.example.proyectopp.service.LogAnalyticsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportsEndpoint {

    private final LogAnalyticsService service;

    // ===== Errores =====
   
   //---------------------------------------------------------------------------------------
   @Operation(
    summary = "Errores por código",
    description = "Devuelve la lista de errores agrupados por código HTTP o nombre de excepción con su cantidad."
)
@ApiResponse(
    responseCode = "200",
    description = "Lista de errores agrupados por código",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ErrorCount.class),
        examples = @ExampleObject(
            value = "[{\"error\":\"404\",\"count\":15},{\"error\":\"500\",\"count\":4}]"
        )
    )
) 
   @GetMapping("/errors/by-code")
    public List<ErrorCount> errorsByCode() { return service.errorCountByCode(); }

  //--------------------------------------------------------------------------------------- 
   @Operation(
    summary = "Top 3 errores",
    description = "Retorna los 3 errores más frecuentes detectados en los logs."
)
@ApiResponse(
    responseCode = "200",
    description = "Lista con los 3 errores más frecuentes",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = TopError.class),
        examples = @ExampleObject(
            value = "[{\"error\":\"404\",\"count\":15},{\"error\":\"NullPointerException\",\"count\":7},{\"error\":\"500\",\"count\":4}]"
        )
    )
)

    @GetMapping("/errors/top3")
    public List<TopError> top3Errors() { return service.top3Errors(); }
//-----------------------------------------------------------------------------------------------
@Operation(
    summary = "Horas pico de errores",
    description = "Devuelve la cantidad de errores agrupados por hora del día."
)
@ApiResponse(
    responseCode = "200",
    description = "Lista de errores agrupados por hora",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = HourlyErrors.class),
        examples = @ExampleObject(
            value = "[{\"hour\":14,\"count\":10},{\"hour\":15,\"count\":8}]"
        )
    )
)   
@GetMapping("/errors/peak-hours")
    public List<HourlyErrors> peakErrorHours() { return service.peakErrorHours(); }

    // ===== Tiempos de Respuesta =====
//-----------------------------------------------------------------------------------------------
@Operation(
    summary = "Estadísticas de tiempos de respuesta",
    description = "Devuelve promedio, mínimo, máximo y mediana de los tiempos de respuesta."
)
@ApiResponse(
    responseCode = "200",
    description = "Estadísticas de tiempos de respuesta",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ResponseStats.class),
        examples = @ExampleObject(
            value = "{\"average\":230.5,\"min\":12,\"max\":1200,\"median\":210.0}"
        )
    )
)    
@GetMapping("/response/stats")
    public ResponseStats responseStats() { return service.responseStats(); }
//-----------------------------------------------------------------------------------------------
    @Operation(
    summary = "Suma de tiempos por endpoint",
    description = "Devuelve la suma de tiempos de respuesta acumulados por cada endpoint."
)
@ApiResponse(
    responseCode = "200",
    description = "Lista con tiempos de respuesta acumulados por endpoint",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = EndpointSum.class),
        examples = @ExampleObject(
            value = "[{\"endpoint\":\"/api/users\",\"sum\":4520},{\"endpoint\":\"/api/login\",\"sum\":2000}]"
        )
    )
)
@GetMapping("/response/sum-by-endpoint")
    public List<EndpointSum> sumByEndpoint() { return service.responseTimeSumByEndpoint(); }

    // ===== Uso de Endpoints =====
    @Operation(
    summary = "Top 3 y Bottom 3 de endpoints",
    description = "Devuelve los 3 endpoints más usados y los 3 menos usados."
)
@ApiResponse(
    responseCode = "200",
    description = "Mapa con endpoints más y menos usados",
    content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(
            value = "{\"mostUsed\":[\"/api/login\",\"/api/users\"],\"leastUsed\":[\"/api/admin\",\"/api/status\"]}"
        )
    )
)
    @GetMapping("/usage/top-bottom")
    public Map<String,Object> usageTopBottom() { return service.endpointUsageTop3AndBottom3(); }
@Operation(
    summary = "Conteo de peticiones por método HTTP",
    description = "Devuelve cuántas peticiones se hicieron por método (GET, POST, PUT, DELETE)."
)
@ApiResponse(
    responseCode = "200",
    description = "Lista de conteos por método HTTP",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = HttpMethodCount.class),
        examples = @ExampleObject(
            value = "[{\"method\":\"GET\",\"count\":1200},{\"method\":\"POST\",\"count\":300}]"
        )
    )
)
    @GetMapping("/usage/by-method")
    public List<HttpMethodCount> byMethod() { return service.countByHttpMethod(); }

    // ===== Alertas / eventos críticos =====
    @Operation(
    summary = "Eventos críticos",
    description = "Devuelve la lista de eventos críticos detectados en los logs."
)
@ApiResponse(
    responseCode = "200",
    description = "Lista de eventos críticos",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = CriticalEvent.class),
        examples = @ExampleObject(
            value = "[{\"id\":\"A1\",\"type\":\"HIGH_ERROR_RATE\",\"description\":\"5 errores 500 en 1 minuto\",\"timestamp\":\"2025-09-20T14:22:00Z\"}]"
        )
    )
)
    @GetMapping("/critical/events")
    public List<CriticalEvent> criticalEvents() { return service.criticalEvents(); }
@Operation(
    summary = "Cantidad de eventos críticos",
    description = "Devuelve el número total de eventos críticos detectados."
)
@ApiResponse(
    responseCode = "200",
    description = "Cantidad total de eventos críticos",
    content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(value = "{\"count\":3}")
    )
)
    @GetMapping("/critical/count")
    public Map<String,Long> criticalCount() { return Map.of("count", service.criticalEventsCount()); }

    // ===== Estado general =====
    @Operation(
    summary = "Estado general de la aplicación",
    description = "Devuelve un resumen con total de peticiones, errores y tiempo promedio de respuesta."
)
@ApiResponse(
    responseCode = "200",
    description = "Estado de la aplicación",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = AppStatus.class),
        examples = @ExampleObject(
            value = "{\"requestsProcessed\":1520,\"errors\":26,\"avgResponseTime\":245.6}"
        )
    )
)
    @GetMapping("/status")
    public AppStatus status() { return service.appStatus(); }
}