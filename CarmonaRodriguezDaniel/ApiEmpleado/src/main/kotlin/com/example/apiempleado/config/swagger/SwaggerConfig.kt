package com.example.apiempleado.config.swagger

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun apiInfo(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("API REST Empleado - Departamento en Spring Boot Reactive")
                    .version("1.0.0")
                    .description("Es una api de empleados creada como practica para aprender en Spring Boot.")
                    .contact(
                        Contact()
                            .name("Daniel Carmona Rodriguez")
                            .email("daniel.carmona@alumno.iesluisvivies.com")
                            .url("https://github.com/Kassius10")
                    )
            )
    }

    @Bean
    fun httpApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("https")
            .pathsToMatch("/**")
            .displayName("HTTPS Api Empleado - Departamento")
            .build()
    }
}