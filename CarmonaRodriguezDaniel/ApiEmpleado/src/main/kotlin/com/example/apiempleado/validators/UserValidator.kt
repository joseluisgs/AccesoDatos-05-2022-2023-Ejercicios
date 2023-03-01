package com.example.apiempleado.validators

import com.example.apiempleado.dto.UsuarioCreateDto
import com.example.apiempleado.exception.UsuarioBadRequestException

fun UsuarioCreateDto.validate(): UsuarioCreateDto {
    if (this.nombre.isBlank()) {
        throw UsuarioBadRequestException("El nombre no puede estar vacío.")
    }
    if (this.email.isBlank()) {
        throw UsuarioBadRequestException("El email no puede estar vacío.")
    }
    if (this.username.isBlank()) {
        throw UsuarioBadRequestException("El username no puede estar vacío.")
    }
    if (this.password.isBlank() || this.password.length < 6) {
        throw UsuarioBadRequestException("La contraseña tiene que ser superior a 6 dígitos.")
    }
    return this
}