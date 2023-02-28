package daniel.rodriguez.ejerciciospring.mappers

import daniel.rodriguez.ejerciciospring.dto.DepartamentoDTO
import daniel.rodriguez.ejerciciospring.dto.DepartamentoDTOcreacion
import daniel.rodriguez.ejerciciospring.models.Departamento
import daniel.rodriguez.ejerciciospring.models.Empleado

fun Departamento.toDTO(empleados: Set<Empleado>) = DepartamentoDTO(
    nombre, presupuesto,
    empleados.map { it.toDTO() }.toList()
)
fun DepartamentoDTOcreacion.fromDTO() =
    Departamento(uuid = id, nombre = nombre, presupuesto = presupuesto)