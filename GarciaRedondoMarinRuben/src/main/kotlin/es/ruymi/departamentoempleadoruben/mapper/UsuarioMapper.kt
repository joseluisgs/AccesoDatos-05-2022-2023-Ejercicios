package es.ruymi.departamentoempleadoruben.mapper

import es.ruymi.departamentoempleadoruben.dto.UserCreateDto
import es.ruymi.departamentoempleadoruben.dto.UserDTO
import es.ruymi.departamentoempleadoruben.models.User

fun User.toDto(): UserDTO {
    return UserDTO(
        id = this.uuid.toString(),
        usuario = this.usuario,
        password = this.password,
        correo = this.correo,
        rol = this.rol.split(",").map { it.trim() }.toSet(),
    )
}

fun UserCreateDto.toEntity(): User {
    return User(
        usuario = this.usuario,
        password = this.password,
        correo = this.correo,
        rol = this.rol.joinToString(", ") { it.uppercase().trim() },
    )
}



