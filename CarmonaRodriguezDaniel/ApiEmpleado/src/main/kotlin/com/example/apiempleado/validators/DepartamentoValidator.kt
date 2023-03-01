package com.example.apiempleado.validators

import com.example.apiempleado.dto.DepartamentoCreateDto
import com.example.apiempleado.exception.DepartamentoBadRequestException

fun DepartamentoCreateDto.validate(): DepartamentoCreateDto {
    if (this.name.isBlank()) {
        throw DepartamentoBadRequestException("El nombre no puede estar vacio.")
    }
    return this
}