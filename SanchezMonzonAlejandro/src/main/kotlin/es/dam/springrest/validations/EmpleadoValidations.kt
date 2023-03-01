package es.dam.springrest.validations

import es.dam.springrest.dto.EmpleadoResponseDTO
import es.dam.springrest.exceptions.EmpleadoBadRequestException

fun EmpleadoResponseDTO.validarCreate(): EmpleadoResponseDTO {
    if(this.nombre.isBlank())
        throw EmpleadoBadRequestException("El nombre no puede ser nulo.")
    if(this.email.isBlank())
        throw EmpleadoBadRequestException("El email no puede ser nulo.")
    if(!this.email.matches(Regex("^[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\$")))
        throw EmpleadoBadRequestException("El email tiene un formato incorrecto")
    if (this.avatar.isBlank()) {
        throw EmpleadoBadRequestException("El avatar no puede ser nulo.")
    }

    return this
}