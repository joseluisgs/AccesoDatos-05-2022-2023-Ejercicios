package resa.mario.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

/**
 * Modelo Empleado
 *
 * @property id
 * @property name
 * @property email
 * @property departamentoId
 * @property avatar
 */
@Table("EMPLEADOS")
data class Empleado(
    @Id
    val id: Long? = null,
    val name: String,
    val email: String,
    @Column("departamento_id")
    var departamentoId: Long? = null,
    var avatar: String?
) {
    override fun toString(): String {
        return "Empleado(id=$id, name='$name', email='$email', departamentoId=$departamentoId)"
    }
}
