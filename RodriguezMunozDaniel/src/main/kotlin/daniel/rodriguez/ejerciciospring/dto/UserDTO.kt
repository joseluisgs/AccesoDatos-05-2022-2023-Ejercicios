package daniel.rodriguez.ejerciciospring.dto

import daniel.rodriguez.ejerciciospring.models.Role
import daniel.rodriguez.ejerciciospring.services.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserDTOcreacion(
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID = UUID.randomUUID(),
    val username: String,
    val email: String,
    val password: String,
    val role: Role = Role.EMPLEADO
)

@Serializable
data class UserDTOlogin(
    val username: String,
    val password: String
)

@Serializable
data class UserDTO(
    val username: String,
    val email: String,
    val role: Role = Role.EMPLEADO
)

@Serializable
data class UserDTOandToken(
    val user: UserDTO,
    val token: String
)