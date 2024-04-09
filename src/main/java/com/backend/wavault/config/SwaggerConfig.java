package com.backend.wavault.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                description = "Open API documentation for WaVault Web Application",
                title = "WaVault web Application",
                contact = @Contact(name = "Olawale Agboola",
                        email = "agboolawale8@gmail.com",
                        url = "https://www.github.com/Realwale"),
                version = "1.0",
                license = @License(name = "Apache License", url = "https://www.apache.org/licenses/LICENSE-2"),
                termsOfService = "Terms of Service"
        ),
        security = {
                @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "Bearer Authentication", scopes = { "global" }),
        }
)

@io.swagger.v3.oas.annotations.security.SecurityScheme(
        name = "Bearer Authentication",
        description = "JWT Authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
}
