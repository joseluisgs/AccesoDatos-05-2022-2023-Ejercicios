package es.dam.springrest.mappers

import es.dam.springrest.dto.EmpleadoResponseDTO
import es.dam.springrest.models.Empleado

fun EmpleadoResponseDTO.toEmpleado(): Empleado {
    return Empleado(
        nombre = nombre,
        email = email,
        avatar = avatar,
        departamento_id = departamento_id
    )
}