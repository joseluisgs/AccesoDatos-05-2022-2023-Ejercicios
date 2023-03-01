package com.example.apiempleado.mapper

import com.example.apiempleado.dto.EmpleadoCreateDto
import com.example.apiempleado.dto.EmpleadoDto
import com.example.apiempleado.dto.EmpleadoUpdateDto
import com.example.apiempleado.model.Departamento
import com.example.apiempleado.model.Empleado

fun EmpleadoCreateDto.toEmpleado(): Empleado {
    return Empleado(
        name = this.name,
        available = true,
        departamento = this.departamentoId
    )
}

fun Empleado.toEmpleadoDto(departamento: Departamento): EmpleadoDto {
    return EmpleadoDto(
        this.id!!,
        this.name,
        departamento
    )
}

fun EmpleadoUpdateDto.toEmpleado(departamento: Departamento): Empleado {
    return Empleado(
        name = this.name,
        available = this.available,
        departamento = departamento.id!!
    )
}