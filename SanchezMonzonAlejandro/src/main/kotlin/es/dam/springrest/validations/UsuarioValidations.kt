package es.dam.springrest.validations

import es.dam.springrest.dto.UsuarioLoginDTO
import es.dam.springrest.dto.UsuarioRegisterDTO
import es.dam.springrest.exceptions.UsuarioBadRequestException

fun UsuarioRegisterDTO.validarCreate(): UsuarioRegisterDTO {
    if (this.username.isBlank())
        throw UsuarioBadRequestException("El username no puede ser nulo.")
    if(this.password.isBlank())
        throw UsuarioBadRequestException("La contraseña no puede ser nula.")

    return this
}