package com.example.apiempleado.config.websocket

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfig : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(webSocketEmpleadoHandler(), "/updates/empleados")
        registry.addHandler(webSocketDepartamentoHandler(), "/updates/departamentos")
    }

    @Bean
    fun webSocketEmpleadoHandler(): WebSocketHandler {
        return WebSocketHandler("Empleados")
    }

    @Bean
    fun webSocketDepartamentoHandler(): WebSocketHandler {
        return WebSocketHandler("Departamentos")
    }
}