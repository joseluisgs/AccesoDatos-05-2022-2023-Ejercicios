package com.example.departemplespring.dto

import com.example.departemplespring.models.Rol
import java.util.*

data class UsuarioLoginDto(
    val username: String,
    val password: String
)

data class UsuarioCreateDto(
    val email: String,
    val username: String,
    val rol: Set<String> = setOf(Rol.USER.name),
    val password: String
)

data class UsuarioDto(
    val id: Long? = null,
    val username: String,
    val email: String,
    val rol: Set<String> = setOf(Rol.USER.name),
)

data class UsuarioTokenDto(
    val user:UsuarioDto,
    val token:String
)