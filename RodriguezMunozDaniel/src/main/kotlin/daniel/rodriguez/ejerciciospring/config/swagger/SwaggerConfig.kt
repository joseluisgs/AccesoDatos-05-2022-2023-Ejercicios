package daniel.rodriguez.ejerciciospring.config.swagger

import daniel.rodriguez.ejerciciospring.config.APIConfig
import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun info(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Ejercicio de Springboot.")
                    .version(APIConfig.API_VERSION)
                    .description("API REST con H2 y SpringBoot para el ejercicio opcional de AD.")
                    .contact(
                        Contact()
                            .name("Daniel Rodriguez Muñoz")
                            .email("daniel.ro.mu02@gmail.com")
                            .url("https://github.com/Idliketobealoli")
                    )
            )
            .externalDocs(
                ExternalDocumentation()
                    .description("Repositorio de GitHub: ")
                    .url("https://github.com/Idliketobealoli/EjercicioSpring")
            )
    }

    @Bean
    fun httpApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("http")
            .pathsToMatch("${APIConfig.API_PATH}/users/**")
            .pathsToMatch("${APIConfig.API_PATH}/storage/**")
            .pathsToMatch("${APIConfig.API_PATH}/empleados/**")
            .pathsToMatch("${APIConfig.API_PATH}/departamentos/**")
            .displayName("Api Ejercicio Springboot")
            .build()
    }
}