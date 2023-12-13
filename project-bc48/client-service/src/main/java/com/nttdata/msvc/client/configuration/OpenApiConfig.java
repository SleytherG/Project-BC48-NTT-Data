package com.nttdata.msvc.client.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
  info = @Info(
    title = "Clients API",
    version = "1.0",
    description = "Documentation for endpoints in Clients API"
  ))
public class OpenApiConfig {
}
