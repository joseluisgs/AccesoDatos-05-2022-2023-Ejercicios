package com.example.apiempleado.validators

import com.example.apiempleado.dto.EmpleadoCreateDto
import com.example.apiempleado.dto.EmpleadoUpdateDto
import com.example.apiempleado.exception.EmpleadoBadRequestException

fun EmpleadoCreateDto.validate(): EmpleadoCreateDto {
    if (this.name.isBlank()) {
        throw EmpleadoBadRequestException("El nombre no puede estar vacío.")
    }
    if (this.departamentoId <= 0) {
        throw EmpleadoBadRequestException("La id de departamento no existe.")
    }
    return this
}

fun EmpleadoUpdateDto.validate(): EmpleadoUpdateDto {
    if (this.name.isBlank()) {
        throw EmpleadoBadRequestException("El nombre no puede estar vacío.")
    }
    return this
}