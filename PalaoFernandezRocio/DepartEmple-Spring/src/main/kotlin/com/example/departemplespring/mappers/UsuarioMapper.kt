package com.example.departemplespring.mappers

import com.example.departemplespring.dto.UsuarioCreateDto
import com.example.departemplespring.dto.UsuarioDto
import com.example.departemplespring.models.Usuario

fun UsuarioCreateDto.toUsuario():Usuario{
    return Usuario(
        id = null,
        email = email,
        username = username,
        password = password,
        rol = rol.joinToString(", "){it.uppercase().trim()}
    )
}

fun Usuario.toDto(): UsuarioDto{
    return UsuarioDto(
        id = id,
        username = username,
        email = email,
        rol = rol.split(",").map { it.trim() }.toSet(),
    )
}