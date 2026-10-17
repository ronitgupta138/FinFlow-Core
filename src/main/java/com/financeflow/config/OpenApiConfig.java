package com.financeflow.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI financeFlowOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FinFlow Core REST API")
                        .description("Production-grade banking and financial aggregation platform with Offline BLE Mesh settlement engine.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Ronit Gupta")
                                .email("ronitgupta138@gmail.com")
                                .url("https://github.com/ronitgupta138/finflow-core"))
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .name("BearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter stateless JWT token obtained from /api/auth/login")));
    }
}
