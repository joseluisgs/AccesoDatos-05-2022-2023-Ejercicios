package es.dam.springrest.validations

import es.dam.springrest.dto.DepartamentoResponseDTO
import es.dam.springrest.exceptions.DepartamentoBadRequestException

fun DepartamentoResponseDTO.validateCreate(): DepartamentoResponseDTO {
    if (this.nombre.isBlank())
        throw DepartamentoBadRequestException("El nombre no puede ser nulo.")
    if(this.presupuesto < 0)
        throw DepartamentoBadRequestException("El presupuesto no puede ser negativo.")

    return this
}