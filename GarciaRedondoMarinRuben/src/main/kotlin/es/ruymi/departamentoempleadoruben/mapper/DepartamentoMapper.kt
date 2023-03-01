package es.ruymi.departamentoempleadoruben.mapper

import es.ruymi.departamentoempleadoruben.dto.DepartamentoDTO
import es.ruymi.departamentoempleadoruben.models.Departamento
import java.util.UUID


fun DepartamentoDTO.toEntity(): Departamento{
    return Departamento(
        uuid = UUID.fromString(this.id),
        nombre = this.nombre,
        presupuesto = this.presupuesto
    )
}

fun Departamento.toDto(): DepartamentoDTO{
    return DepartamentoDTO(
        id = uuid.toString(),
        nombre = this.nombre,
        presupuesto = this.presupuesto
    )
}