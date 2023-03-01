package es.ruymi.departamentoempleadoruben.dto

import es.ruymi.departamentoempleadoruben.models.User



data class UserDTO(
    val id: String,
    val usuario: String,
    val correo: String,
    val password: String,
    val rol: Set<String> = setOf(User.Rol.USER.name),
)


data class UserUpdateDto(
    val correo: String,
    val usuario: String,
)


data class UserCreateDto(
    val correo: String,
    val usuario: String,
    val password: String,
    val rol: Set<String> = setOf(User.Rol.USER.name),
)


data class UserLoginDto(
    val usuario: String,
    val password: String
)


data class UserWithTokenDto(
    val usuario: UserDTO,
    val token: String
)