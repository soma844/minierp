package de.hafni.minierp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI miniErpOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("MiniERP API")
                        .description("""
                                REST-API für ein MiniERP zur technischen
                                Lichtkuppel-Konfiguration, Prüfung,
                                Auftragsabwicklung, Produktion,
                                Controlling und Buchhaltung.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Ismail Hafni"))
                        .license(new License()
                                .name("Portfolio / Demo Project")));
    }
}