package daniel.rodriguez.ejerciciospring.models

import jakarta.validation.constraints.NotEmpty
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "empleados")
data class Empleado(
    @Id
    val id: Long? = null,
    val uuid: UUID? = UUID.randomUUID(),
    @NotEmpty(message = "Must have a name.")
    val nombre: String,
    @NotEmpty(message = "Must have an email.")
    val email: String,
    val avatar: String,
    @NotEmpty(message = "Must have an associated department.")
    @Column("departamento_id")
    val departamentoId: UUID
)