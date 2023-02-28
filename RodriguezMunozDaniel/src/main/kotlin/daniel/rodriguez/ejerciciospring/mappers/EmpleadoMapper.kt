package daniel.rodriguez.ejerciciospring.mappers

import daniel.rodriguez.ejerciciospring.dto.EmpleadoDTO
import daniel.rodriguez.ejerciciospring.dto.EmpleadoDTOcreacion
import daniel.rodriguez.ejerciciospring.models.Empleado

fun Empleado.toDTO() = EmpleadoDTO(nombre, email, avatar, departamentoId)
fun EmpleadoDTOcreacion.fromDTO() = Empleado(
    uuid = id, nombre = nombre, email = email,
    avatar = avatar, departamentoId = departamentoId)