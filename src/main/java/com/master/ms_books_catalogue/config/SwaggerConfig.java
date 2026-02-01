package com.master.ms_books_catalogue.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Books Catalogue API")
                        .version("1.0.0")
                        .description("API REST para el catálogo de libros del proyecto Relatos de Papel. " +
                                "Permite gestionar libros con operaciones CRUD, búsquedas filtradas y soft delete.")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("contacto@master.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")));
    }
}