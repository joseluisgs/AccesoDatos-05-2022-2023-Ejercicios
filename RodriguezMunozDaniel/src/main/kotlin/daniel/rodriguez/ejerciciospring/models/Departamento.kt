package daniel.rodriguez.ejerciciospring.models

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "departamentos")
data class Departamento(
    @Id
    val id: Long? = null,
    val uuid: UUID? = UUID.randomUUID(),
    @NotEmpty(message = "Must have a name.")
    val nombre: String,
    @Min(0)
    val presupuesto: Double = 0.0
)