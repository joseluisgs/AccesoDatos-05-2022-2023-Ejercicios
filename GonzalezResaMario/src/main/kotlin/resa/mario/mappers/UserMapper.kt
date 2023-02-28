package resa.mario.mappers

import resa.mario.dto.UsuarioDTORegister
import resa.mario.dto.UsuarioDTOResponse
import resa.mario.models.Usuario

/**
 * Mapper Model -> DTO
 *
 * @return [UsuarioDTOResponse]
 */
fun Usuario.toDTO(): UsuarioDTOResponse {
    return UsuarioDTOResponse(
        username = username,
        role = role,
        createdAt = createdAt.toString()
    )
}

/**
 * Mapper DTO -> Model
 *
 * @return [Usuario]
 */
fun UsuarioDTORegister.toUsuario(): Usuario {
    return Usuario(
        username = username,
        password = password,
        role = role
    )
}