package daniel.rodriguez.ejerciciospring.repositories.empleado

import daniel.rodriguez.ejerciciospring.models.Empleado
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface EmpleadoRepository : CoroutineCrudRepository<Empleado, Long> {
    fun findByUuid(uuid: UUID): Flow<Empleado>
}