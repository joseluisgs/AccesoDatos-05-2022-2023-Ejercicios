package mireya.sanchez.apispring.validations

import mireya.sanchez.apispring.dto.UsuarioRegisterDTO
import mireya.sanchez.apispring.exceptions.UsuarioBadRequestException

fun UsuarioRegisterDTO.validate():UsuarioRegisterDTO{
    if (this.username.isBlank())
        throw UsuarioBadRequestException("El nombre de usuario no puede estar vacío.")
    if(this.password.isBlank())
        throw UsuarioBadRequestException("La contraseña no puede estar vacía.")
    if(this.password.length <= 4)
        throw UsuarioBadRequestException("La contraseña debe de tener mas de 4 caracteres.")
    return this
}
