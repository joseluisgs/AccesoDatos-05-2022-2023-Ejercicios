package mireya.sanchez.apispring.models

import kotlinx.serialization.Serializable
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Serializable
@Table("Departamentos")
data class Departamento(
    @Id
    val id: Long? = null,
    val nombre: String,
    val presupuesto: Double
)