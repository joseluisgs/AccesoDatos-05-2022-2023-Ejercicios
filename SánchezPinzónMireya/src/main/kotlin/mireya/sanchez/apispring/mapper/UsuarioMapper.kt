package mireya.sanchez.apispring.mapper

import mireya.sanchez.apispring.dto.UsuarioDTO
import mireya.sanchez.apispring.dto.UsuarioRegisterDTO
import mireya.sanchez.apispring.models.Usuario

fun Usuario.toDTO(): UsuarioDTO {
    return UsuarioDTO(
        username = username,
        rol = rol,
        createdAt = createdAt.toString()
    )
}

fun UsuarioRegisterDTO.toUsuario(): Usuario {
    return Usuario(
        username = username,
        password = password,
        rol = rol
    )
}