package com.nttdata.msvc.product.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
  info = @Info(
    title = "Product API",
    version = "1.0",
    description = "Documentation for endpoints of Product API")
)
public class OpenApiConfig {
}
