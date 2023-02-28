package resa.mario.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

/**
 * Modelo Departamento
 *
 * @property id
 * @property nombre
 * @property presupuesto
 */
@Table("DEPARTAMENTOS")
data class Departamento(
    @Id
    val id: Long? = null,
    val nombre: String,
    val presupuesto: Double
) {
    override fun toString(): String {
        return "Departamento(id=$id, nombre='$nombre', presupuesto=$presupuesto)"
    }
}
