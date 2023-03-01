package es.ruymi.departamentoempleadoruben.dto

import java.util.*

data class EmpleadoDTO(
    val id: String? = UUID.randomUUID().toString(),
    val nombre: String,
    val email: String,
    val avatar: String,
    val departamento: UUID
)


