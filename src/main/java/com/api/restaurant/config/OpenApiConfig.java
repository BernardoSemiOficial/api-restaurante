package com.api.restaurant.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI restaurantOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Restaurant API")
                        .description("""
                                API REST para gestão de clientes e donos de restaurante.
                                
                                Recursos principais:
                                - Cadastro, atualização, exclusão e busca por nome
                                - Troca de senha em endpoint separado
                                - Login unificado para cliente e dono
                                - Erros padronizados com ProblemDetail (RFC 7807)
                                
                                Versionamento via path: `/api/v1`
                                """)
                        .version("v1")
                        .contact(new Contact().name("API Restaurant")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local")
                ));
    }
}
