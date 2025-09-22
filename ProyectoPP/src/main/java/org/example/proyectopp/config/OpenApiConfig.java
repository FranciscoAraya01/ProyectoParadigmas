package org.example.proyectopp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info()
                .title("API – Procesamiento Funcional de Logs")
                .version("v1")
                .description("Esqueleto de endpoints; lógica pendiente con Streams"));
    }
}