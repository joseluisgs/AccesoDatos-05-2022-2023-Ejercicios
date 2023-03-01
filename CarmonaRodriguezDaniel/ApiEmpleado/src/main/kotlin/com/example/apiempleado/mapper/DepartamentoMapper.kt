package com.example.apiempleado.mapper

import com.example.apiempleado.dto.DepartamentoCreateDto
import com.example.apiempleado.dto.DepartamentoDto
import com.example.apiempleado.dto.DepartamentoUpdateDto
import com.example.apiempleado.model.Departamento


fun DepartamentoCreateDto.toDepartamento(): Departamento {
    return Departamento(
        name = this.name,
    )
}

fun DepartamentoUpdateDto.toDepartamento(): Departamento {
    return Departamento(
        id = null,
        name = this.name,
    )
}

fun Departamento.toDepartamentoDto(): DepartamentoDto {
    return DepartamentoDto(
        name = this.name
    )
}