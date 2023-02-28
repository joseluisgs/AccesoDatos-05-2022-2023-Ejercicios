package mireya.sanchez.apispring.dto

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioLoginDTO(
    val username: String,
    val password: String
)

@Serializable
data class UsuarioRegisterDTO(
    val username: String,
    val password: String,
    val rol: String,
)

@Serializable
data class UsuarioDTO(
    val username: String,
    val rol: String,
    val createdAt: String
)