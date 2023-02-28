package resa.mario.config

import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// HABRA QUE PERMITIR EL ACCESO EN SECURITYCONFIG A LAS RUTAS DE SWAGGER
/**
 * Clase de configuracion para Swagger-OpenAPI
 *
 */
@Configuration
class SwaggerConfig {
    @Bean
    fun apiInfo(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("API REST Empleado-Departamento con usuarios Spring")
                    .version("1.0.0")
                    .description("Ejercicio opcional PSP-AD sobre una API REST realizada con Spring. Curso 22/23 DAM")

            )
            .externalDocs(
                ExternalDocumentation()
                    .description("Repositorio del proyecto en Github")
                    .url("https://github.com/Mario999X/EmpleadoDepartamentoSpringOpcional")
            )
    }


    @Bean
    fun httpApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("http")
            .pathsToMatch("/api/**")
            .displayName("HTTP-API Empleado-Departameto Spring")
            .build()
    }
}