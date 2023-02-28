package mendoza.acosta.empresarestspringboot.config

import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
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
                    .title("Empresa Rest SpringBoot reactivo")
                    .version("lastes")
                    .description("API Rest con SpringBoot en Kotlin")
            )
            .externalDocs(
                ExternalDocumentation()
                    .description("Repositorio y documentación de la API")
                    .url("https://github.com/SebsMendoza/Empresa-Rest-SpringBoot")
            )
    }

    @Bean
    fun httpApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("http")
            .pathsToMatch("/api/**")
            .displayName("HTTP-API Empresa Rest SpringBoot")
            .build()
    }
}