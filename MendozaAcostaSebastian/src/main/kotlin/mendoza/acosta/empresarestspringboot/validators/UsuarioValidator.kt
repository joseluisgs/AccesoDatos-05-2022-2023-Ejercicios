package mendoza.acosta.empresarestspringboot.validators

import mendoza.acosta.empresarestspringboot.dto.UsuarioCreateDto
import mendoza.acosta.empresarestspringboot.dto.UsuarioUpdateDto
import mendoza.acosta.empresarestspringboot.exception.UsuarioBadRequestException

fun UsuarioCreateDto.validate(): UsuarioCreateDto {
    if (this.nombre.isBlank()) {
        throw UsuarioBadRequestException("El nombre no puede estar vacío")
    } else if (this.email.isBlank() || !this.email.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")))
        throw UsuarioBadRequestException("El email no puede estar vacío o no está en el formato correcto")
    else if (this.username.isBlank())
        throw UsuarioBadRequestException("El username no puede estar vacío")
    else if (this.password.isBlank() || this.password.length < 5)
        throw UsuarioBadRequestException("El password no puede estar vacío o tener menos de 5 caracteres")
    return this
}

fun UsuarioUpdateDto.validate(): UsuarioUpdateDto {
    if (this.nombre.isBlank()) {
        throw UsuarioBadRequestException("El nombre no puede estar vacío")
    } else if (this.email.isBlank() || !this.email.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")))
        throw UsuarioBadRequestException("El email no puede estar vacío o no está en el formato correcto")
    else if (this.username.isBlank())
        throw UsuarioBadRequestException("El usuario no puede estar vacío")
    return this
}