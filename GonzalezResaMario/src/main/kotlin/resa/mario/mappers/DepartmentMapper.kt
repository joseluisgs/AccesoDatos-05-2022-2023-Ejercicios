package resa.mario.mappers

import resa.mario.dto.DepartamentoDTO
import resa.mario.models.Departamento

/**
 * Mapper Model -> DTO
 *
 * @return [DepartamentoDTO]
 */
fun Departamento.toDTO(): DepartamentoDTO {
    return DepartamentoDTO(
        nombre, presupuesto
    )
}

/**
 * Mapper DTO -> Model
 *
 * @return [Departamento]
 */
fun DepartamentoDTO.toDepartamento(): Departamento {
    return Departamento(
        nombre = nombre,
        presupuesto = presupuesto
    )
}