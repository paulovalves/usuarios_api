package com.teste.vm_tecnologia.config;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Teste VM Tecnologia API")
                        .version("1.0")
                        .description("API para cadastro de usuários.")
                        .contact(new Contact()
                                .name("Paulo Alves")
                                .url("https://www.linkedin.com/in/paulo-victor-alves")
                                .email("paulovalves@outlook.com"))
                )
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .components(new io.swagger.v3.oas.models.Components().addSecuritySchemes("basicAuth",
                        new io.swagger.v3.oas.models.security.SecurityScheme()
                                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                .scheme("basic")
                                .description("Basic authentication"))
                );
    }
}
