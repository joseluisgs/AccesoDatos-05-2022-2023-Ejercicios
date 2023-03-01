package com.example.departemplespring.validators

import com.example.departemplespring.dto.UsuarioCreateDto
import com.example.departemplespring.dto.UsuarioLoginDto
import com.example.departemplespring.exceptions.UsuarioBadRequestException

fun UsuarioCreateDto.validarCreate():UsuarioCreateDto{
    if (this.username.isBlank())
        throw UsuarioBadRequestException("El username no puede estar vacío")
    if (this.email.isBlank())
        throw UsuarioBadRequestException("El email no puede estar vacío")
    if(!this.email.matches(Regex("^[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\$")))
        throw IllegalArgumentException("Correo incorrecto")
    if(this.password.isBlank())
        throw UsuarioBadRequestException("La contraseña no puede estar vacía")

    return this
}


fun UsuarioLoginDto.validarLogin(): UsuarioLoginDto{
    if (this.username.isBlank())
    throw UsuarioBadRequestException("El username no puede estar vacío")
    if(this.password.isBlank())
        throw UsuarioBadRequestException("La contraseña no puede estar vacía")
    return this
}