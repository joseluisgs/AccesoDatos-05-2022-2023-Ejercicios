package es.ruymi.departamentoempleadoruben.models

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "EMPLEADO")
data class Empleado(
    @Id
    val id: Long? = null,
    val uuid: UUID = UUID.randomUUID(),
    @NotEmpty(message = "El nombre no puede estar vacío")
    val nombre: String,
    @Email(regexp = ".*@.*\\..*", message = "El email debe ser un email valido")
    val email: String,
    val avatar: String,
    @NotEmpty(message = "El empleado deber estar asignado a un departamento")
    val departamento_id: UUID
) {
}


