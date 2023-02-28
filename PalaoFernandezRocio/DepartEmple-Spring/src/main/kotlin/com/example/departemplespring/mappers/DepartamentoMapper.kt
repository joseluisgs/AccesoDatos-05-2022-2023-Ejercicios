package com.example.departemplespring.mappers

import com.example.departemplespring.dto.DepartamentoCreateDto
import com.example.departemplespring.models.Departamento
import java.util.*

fun DepartamentoCreateDto.toDepartamento(): Departamento{
    return Departamento(
        id = null,
        uuid = UUID.randomUUID(),
        nombre = nombre,
        presupuesto = presupuesto
    )
}