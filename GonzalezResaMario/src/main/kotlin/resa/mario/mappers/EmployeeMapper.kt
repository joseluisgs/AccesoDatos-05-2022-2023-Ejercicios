package resa.mario.mappers

import resa.mario.dto.EmpleadoDTO
import resa.mario.models.Empleado

/**
 * Mapper Model -> DTO
 *
 * @return [EmpleadoDTO]
 */
fun Empleado.toDTO(): EmpleadoDTO {
    return EmpleadoDTO(
        name = name,
        email = email,
        departamentoId = departamentoId.toString(),
        avatar = avatar
    )
}

/**
 * Mapper DTO -> Model
 *
 * @return [Empleado]
 */
fun EmpleadoDTO.toEmpleado(): Empleado {
    return Empleado(
        name = name,
        email = email,
        departamentoId = departamentoId?.toLong(),
        avatar = avatar ?: "https://cdn-icons-png.flaticon.com/512/2550/2550260.png"
    )
}