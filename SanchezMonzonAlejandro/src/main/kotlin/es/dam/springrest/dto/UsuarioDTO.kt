package es.dam.springrest.dto

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
    val role: String,
)

@Serializable
data class UsuarioResponseDTO(
    val username: String,
    val role: String,
    val createdAt: String
)