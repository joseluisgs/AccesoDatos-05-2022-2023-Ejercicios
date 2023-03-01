package com.example.departemplespring.validators

import com.example.departemplespring.dto.DepartamentoCreateDto
import com.example.departemplespring.exceptions.DepartamentoBadRequestException

fun DepartamentoCreateDto.validateCreate(): DepartamentoCreateDto{
    if (this.nombre.isBlank())
        throw DepartamentoBadRequestException("El nombre del departamento no puede estar vacío")
    if(this.presupuesto < 0)
        throw DepartamentoBadRequestException("El presupuesto del departamento no puede ser negativo")
    return this
}