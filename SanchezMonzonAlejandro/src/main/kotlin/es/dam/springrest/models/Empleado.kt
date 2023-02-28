package es.dam.springrest.models

import kotlinx.serialization.Serializable
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Serializable
@Table("empleados")
data class Empleado(
    @Id
    val id: Long? = null,
    val nombre: String,
    val email: String,
    val avatar: String,
    val departamento_id: Long
)
