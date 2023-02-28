package mendoza.acosta.empresarestspringboot.models

import jakarta.validation.constraints.NotEmpty
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.Random
import java.util.UUID

@Table(name = "empleados")
data class Empleado(
    @Id
    val id: Long? = null,
    val uuid: UUID = UUID.randomUUID(),
    @NotEmpty(message = "El nombre no puede estar vacío")
    val nombre: String,
    @NotEmpty(message = "El email no puede estar vacío")
    val email: String,
    val salario: Double = Random().nextDouble(1000.0, 4000.0),
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column("updated_at")
    val updatedAt: LocalDateTime? = null,
    val deleted: Boolean = false
)