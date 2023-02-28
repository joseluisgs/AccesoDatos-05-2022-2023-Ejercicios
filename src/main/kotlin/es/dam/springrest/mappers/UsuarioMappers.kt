package es.dam.springrest.mappers

import es.dam.springrest.dto.UsuarioRegisterDTO
import es.dam.springrest.dto.UsuarioResponseDTO
import es.dam.springrest.models.Usuario

fun Usuario.toDTO(): UsuarioResponseDTO {
    return UsuarioResponseDTO(
        username = username,
        role = role,
        createdAt = createdAt.toString()
    )
}

fun UsuarioRegisterDTO.toModel(): Usuario {
    return Usuario(
        username = username,
        password = password,
        role = role
    )
}