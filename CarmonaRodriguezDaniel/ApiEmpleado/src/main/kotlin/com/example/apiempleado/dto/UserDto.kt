package com.example.apiempleado.dto

import com.example.apiempleado.model.Usuario
import kotlinx.serialization.Serializable

data class UsuarioCreateDto(
    val nombre: String,
    val email: String,
    val username: String,
    val avatar: String? = null,
    val rol: Set<String> = setOf(Usuario.Rol.USER.name),
    val password: String,
)

data class UsuarioDto(
    val nombre: String,
    val email: String,
    val username: String,
    val avatar: String? = null,
)

data class UsuarioDtoWithToken(
    val nombre: String,
    val email: String,
    val username: String,
    val avatar: String? = null,
    val token: String,
)

@Serializable
data class UsuarioLoginDto(
    var username: String,
    var password: String,
)