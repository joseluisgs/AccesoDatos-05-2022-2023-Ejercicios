package mireya.sanchez.apispring.models

import kotlinx.serialization.Serializable
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Serializable
@Table("Empleados")
data class Empleado(
    @Id
    val id: Long? = null,
    var avatar: String?,
    val nombre: String,
    val email: String,
    @Column("departamento_id")
    var departamentoId: Long? = null
)