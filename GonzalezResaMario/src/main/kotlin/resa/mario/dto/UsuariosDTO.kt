package resa.mario.dto

import kotlinx.serialization.Serializable

/**
 * DTO de usuarios para el login
 *
 * @property username
 * @property password
 */
@Serializable
data class UsuarioDTOLogin(
    val username: String,
    val password: String
)

/**
 * DTO de usuarios para el registro
 *
 * @property username
 * @property password
 * @property role
 */
@Serializable
data class UsuarioDTORegister(
    val username: String,
    val password: String,
    val role: String,
)

/**
 * DTO de usuarios
 *
 * @property username
 * @property role
 * @property createdAt
 */
@Serializable
data class UsuarioDTOResponse(
    val username: String,
    val role: String,
    val createdAt: String
)