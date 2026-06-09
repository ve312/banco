package com.trinity.banco.shared.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Banco API",
                version = "1.0",
                description = "API para gestión de clientes, cuentas y transacciones bancarias"
        )
)
public class OpenApiConfig {
}
