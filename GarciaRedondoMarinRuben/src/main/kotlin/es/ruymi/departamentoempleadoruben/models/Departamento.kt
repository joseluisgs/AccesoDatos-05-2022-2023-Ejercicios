package es.ruymi.departamentoempleadoruben.models

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Table(name = "DEPARTAMENTO")
data class Departamento(
    @Id
    val id: Long? = null,
    val uuid: UUID = UUID.randomUUID(),
    @NotEmpty(message = "El nombre del departamento no puede estar vacío")
    var nombre: String,
    @Min(value = 1, message = "El presupuesto debe ser mayor a 0")
    val presupuesto: Double
) {

}