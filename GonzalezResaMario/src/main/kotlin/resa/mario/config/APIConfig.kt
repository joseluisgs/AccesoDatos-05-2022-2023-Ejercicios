package resa.mario.config

import org.springframework.context.annotation.Configuration

/**
 * Clase de configuracion con informacion basica de la API
 *
 */
@Configuration
class APIConfig {
    companion object {
        const val API_PATH = "/api"

        const val API_VERSION = "1.0"

    }
}