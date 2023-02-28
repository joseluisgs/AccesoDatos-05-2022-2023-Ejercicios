package es.ruymi.departamentoempleadoruben.validator

import es.ruymi.departamentoempleadoruben.dto.UserCreateDto
import es.ruymi.departamentoempleadoruben.dto.UserDTO
import es.ruymi.departamentoempleadoruben.dto.UserLoginDto
import es.ruymi.departamentoempleadoruben.dto.UserUpdateDto
import es.ruymi.departamentoempleadoruben.exceptions.UserBadRequestException

fun UserDTO.validate(): UserDTO {
    if (this.usuario.isBlank()) {
        throw UserBadRequestException("El usuario no puede estar vacío")
    } else if (this.correo.isBlank() || !this.correo.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\$")))
        throw UserBadRequestException("El correo no puede estar vacío o no tiene el formato correcto")
    else if (this.password.isBlank()){
        throw UserBadRequestException("La contraseña no puede estar vacía")
    }
    return this
}

fun UserLoginDto.validate(): UserLoginDto {
    if (this.usuario.isBlank()) {
        throw UserBadRequestException("El usuario no puede estar vacío")
    } else if (this.password.isBlank()){
        throw UserBadRequestException("La contraseña no puede estar vacía")
    }
    return this
}

fun UserCreateDto.validate(): UserCreateDto {
    if (this.usuario.isBlank()) {
        throw UserBadRequestException("El usuario no puede estar vacío")
    } else if (this.correo.isBlank() || !this.correo.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\$")))
        throw UserBadRequestException("El correo no puede estar vacío o no tiene el formato correcto")
    else if (this.password.isBlank()){
        throw UserBadRequestException("La contraseña no puede estar vacía")
    }
    return this
}

fun UserUpdateDto.validate(): UserUpdateDto {
    if (this.usuario.isBlank()) {
        throw UserBadRequestException("El usuario no puede estar vacío")
    } else if (this.correo.isBlank() || !this.correo.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\$")))
        throw UserBadRequestException("El correo no puede estar vacío o no tiene el formato correcto")
    return this
}

