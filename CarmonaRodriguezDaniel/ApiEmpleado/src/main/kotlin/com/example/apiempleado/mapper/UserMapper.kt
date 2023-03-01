package com.example.apiempleado.mapper

import com.example.apiempleado.dto.UsuarioCreateDto
import com.example.apiempleado.dto.UsuarioDto
import com.example.apiempleado.dto.UsuarioDtoWithToken
import com.example.apiempleado.model.Usuario

fun UsuarioCreateDto.toUsuario(): Usuario {
    return Usuario(
        nombre = this.nombre,
        email = this.email,
        username = this.username,
        password = this.password,
        avatar = this.avatar ?: "https://upload.wikimedia.org/wikipedia/commons/f/f4/User_Avatar_2.png",
        rol = this.rol.joinToString(", ") { it.uppercase().trim() },
    )
}

fun Usuario.toUsuarioDto(): UsuarioDto {
    return UsuarioDto(
        this.nombre,
        this.email,
        this.username,
        this.avatar
    )
}

fun Usuario.toUsuarioDtoWithToken(token: String): UsuarioDtoWithToken {
    return UsuarioDtoWithToken(
        this.nombre,
        this.email,
        this.username,
        this.avatar,
        token
    )
}