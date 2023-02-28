package daniel.rodriguez.ejerciciospring.dto

import daniel.rodriguez.ejerciciospring.services.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class EmpleadoDTOcreacion(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    val nombre: String,
    val email: String,
    val avatar: String,
    @Serializable(with = UUIDSerializer::class)
    val departamentoId: UUID
)

@Serializable
data class EmpleadoDTO(
    val nombre: String,
    val email: String,
    val avatar: String,
    @Serializable(with = UUIDSerializer::class)
    val departamentoId: UUID
)