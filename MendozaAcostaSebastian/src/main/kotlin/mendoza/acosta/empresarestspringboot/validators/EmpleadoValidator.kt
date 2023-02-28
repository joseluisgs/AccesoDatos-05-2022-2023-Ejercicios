package mendoza.acosta.empresarestspringboot.validators

import mendoza.acosta.empresarestspringboot.dto.EmpleadoCreateDto
import mendoza.acosta.empresarestspringboot.exception.EmpleadoBadRequestException

fun EmpleadoCreateDto.validate(): EmpleadoCreateDto {
    if (this.nombre.isBlank()) {
        throw EmpleadoBadRequestException("El nombre del usuario no puede estar vacío")
    } else if (this.email.isBlank() || !this.email.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")))
        throw EmpleadoBadRequestException("El email no puede estar vacío o no está en el formato correcto")
    return this
}