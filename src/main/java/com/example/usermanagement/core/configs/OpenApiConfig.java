package com.example.usermanagement.core.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Suvorov Vassilievitch
 * Date: 06/04/2025
 * Time: 02:29
 * Project Name: user-management
 */
@Configuration
public class OpenApiConfig {

    @Value("${url.doc.local}")
    private String localUrlPath;

    @Value("${url.doc.integration}")
    private String integUrlPath;
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pyramid API Authorization")
                        .version("1.0.0")
                        .description("API description")
                        .contact(new Contact()
                                .name("Vassilievitch SUVOROV")
                                .email("suvorov22002@gmail.com")
                                .url("https://api.pyramid-distributions.cm"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .addServersItem(new Server().url(localUrlPath).description("Development server"))
                .addServersItem(new Server().url(integUrlPath).description("Production server"));
                /*
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));

                 */
    }
}
