package com.nttdata.msvc.movement.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
  info = @Info(
    title = "Movements API",
    version = "1.0",
    description = "Documentation for endpoints of Movements API")
)
public class OpenApiConfig {
}
