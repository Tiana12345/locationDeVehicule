package com.accenture.configuration.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfiguration {


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Location de véhicule")
                        .version("1.0")
                        .description("Interface de gestion de l'application de location de vehicule")
                        .contact(new Contact()
                                .name("Tatiana")
                                .email("tatiana.m.tessier@accenture.com")
                                .url("http://localhost:8080/swagger-ui/index.html?continue#/")))
                .components(new Components()
                        .addSecuritySchemes("basicAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"));

    }
//                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
//                .components(new io.swagger.v3.oas.models.Components()
//                        .addSecuritySchemes("basicAuth", new SecurityScheme()
//                                .type(SecurityScheme.Type.HTTP)
//                                .scheme("basic")));


    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .addOperationCustomizer((operation, handlerMethod) ->
                        operation.addSecurityItem(new SecurityRequirement().addList("bearerAuth")))
                .build();
    }
}