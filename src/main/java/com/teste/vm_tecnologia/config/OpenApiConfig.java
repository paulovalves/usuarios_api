package com.teste.vm_tecnologia.config;

import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
                );


    }
    private static final Set<String> ALLOWED_SCHEMAS = Set.of(
            "UsuarioEntradaDTO",
            "UsuarioSaidaDTO"
    );
    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        return openApi -> {
            Map<String, Schema> schemas = openApi.getComponents().getSchemas();
            if (schemas != null) {
                Map<String, Schema> filteredSchemas = new HashMap<>();
                for (Map.Entry<String, Schema> entry : schemas.entrySet()) {
                    if (ALLOWED_SCHEMAS.contains(entry.getKey())) {
                        filteredSchemas.put(entry.getKey(), entry.getValue());
                    }
                }
                openApi.getComponents().setSchemas(filteredSchemas);
            }
        };
    }
}
