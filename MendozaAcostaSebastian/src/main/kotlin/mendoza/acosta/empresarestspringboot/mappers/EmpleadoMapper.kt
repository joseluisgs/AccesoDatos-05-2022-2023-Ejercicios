package mendoza.acosta.empresarestspringboot.mappers

import mendoza.acosta.empresarestspringboot.dto.EmpleadoCreateDto
import mendoza.acosta.empresarestspringboot.dto.EmpleadoDto
import mendoza.acosta.empresarestspringboot.models.Empleado

fun Empleado.toDto(): EmpleadoDto {
    return EmpleadoDto(
        id = this.uuid,
        nombre = this.nombre,
        email = this.email,
        salario = this.salario,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString(),
        deleted = this.deleted
    )
}

fun EmpleadoCreateDto.toModel(): Empleado {
    return Empleado(
        nombre = this.nombre,
        email = this.email,
    )
}