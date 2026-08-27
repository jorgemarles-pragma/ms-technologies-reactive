package com.pragma.jamarlesf.mstecnologias.infrastructure.documentation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadatos de Swagger UI. Las claves {@code appDescription} y {@code appVersion} ya existen
 * en application.yaml.
 */
@Configuration
public class OpenApiConfiguration {

    private static final String TITLE = "ms-tecnologias API";

    @Bean
    public OpenAPI technologiesOpenApi(@Value("${appDescription}") String appDescription,
                                       @Value("${appVersion}") String appVersion) {
        return new OpenAPI().info(new Info()
                .title(TITLE)
                .description(appDescription)
                .version(appVersion));
    }
}
