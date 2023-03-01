package com.example.apiempleado.model

import java.time.LocalDateTime

data class Notification<T>(
    val entity: String,
    val tipo: Tipo,
    val id: Long,
    val data: T,
    val createdAt: String = LocalDateTime.now().toString(),
) {
    enum class Tipo {
        CREATE, UPDATE, DELETE
    }
}
