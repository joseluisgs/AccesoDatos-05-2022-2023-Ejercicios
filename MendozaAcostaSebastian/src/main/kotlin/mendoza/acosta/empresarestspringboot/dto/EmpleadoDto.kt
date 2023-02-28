package mendoza.acosta.empresarestspringboot.dto

import java.time.LocalDateTime
import java.util.*

data class EmpleadoCreateDto(
    val nombre: String,
    val email: String,
)

data class EmpleadoDto(
    val id: UUID? = null,
    val nombre: String,
    val email: String,
    val salario: Double,
    val createdAt: String? = null,
    val updatedAt: String? = LocalDateTime.now().toString(),
    val deleted: Boolean = false
)