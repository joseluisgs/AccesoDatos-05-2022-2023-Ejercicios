package mendoza.acosta.empresarestspringboot.dto

import mendoza.acosta.empresarestspringboot.models.Usuario
import java.time.LocalDateTime

data class UsuarioCreateDto(
    val nombre: String,
    val email: String,
    val username: String,
    val avatar: String? = null,
    val rol: Usuario.Rol = Usuario.Rol.USER,
    val password: String
)

data class UsuarioLoginDto(
    val username: String,
    val password: String
)

data class UsuarioDto(
    val nombre: String,
    val username: String,
    val password: String,
    val email: String,
    val avatar: String,
    val rol: Usuario.Rol,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val deleted: Boolean = false
)

data class UsuarioUpdateDto(
    val nombre: String,
    val email: String,
    val username: String
)

data class UserWithTokenDto(
    val usuario: UsuarioDto,
    val token: String
)