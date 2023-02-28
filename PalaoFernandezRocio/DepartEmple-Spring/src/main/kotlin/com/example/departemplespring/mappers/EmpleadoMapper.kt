package com.example.departemplespring.mappers

import com.example.departemplespring.dto.EmpleadoCreateDto
import com.example.departemplespring.exceptions.EmpleadoBadRequestException
import com.example.departemplespring.models.Empleado
import java.util.*

fun EmpleadoCreateDto.toEmpleado(): Empleado {
    try {
        return Empleado(
            id = null,
            nombre = nombre,
            email = email,
            uuidDepartamento = UUID.fromString(uuidDepartamento)
        )
    }catch (e : IllegalArgumentException){
        throw EmpleadoBadRequestException("UUID de departamento incorrecto")
    }
}