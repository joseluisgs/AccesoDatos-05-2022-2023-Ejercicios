package es.dam.springrest.mappers

import es.dam.springrest.dto.DepartamentoResponseDTO
import es.dam.springrest.models.Departamento

fun DepartamentoResponseDTO.toDepartamento(): Departamento {
    return Departamento(
        nombre = nombre,
        presupuesto = presupuesto
    )
}