package es.ruymi.departamentoempleadoruben.mapper

import es.ruymi.departamentoempleadoruben.dto.EmpleadoDTO
import es.ruymi.departamentoempleadoruben.models.Empleado
import java.util.UUID


fun EmpleadoDTO.toEntity(): Empleado{
    return Empleado(
        uuid = UUID.fromString(this.id),
        nombre = this.nombre,
        email = this.email,
        avatar = this.avatar,
        departamento_id = this.departamento
    )
}

fun Empleado.toDto(): EmpleadoDTO{
    return EmpleadoDTO(
        id = this.uuid.toString(),
        nombre = this.nombre,
        email = this.email,
        avatar = this.avatar,
        departamento = this.departamento_id
    )
}