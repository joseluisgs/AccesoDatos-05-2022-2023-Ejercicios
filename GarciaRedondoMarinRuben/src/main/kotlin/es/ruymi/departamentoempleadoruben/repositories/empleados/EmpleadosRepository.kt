package es.ruymi.departamentoempleadoruben.repositories.empleados

import es.ruymi.departamentoempleadoruben.models.Empleado
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface EmpleadosRepository: CoroutineCrudRepository<Empleado, Long> {
    fun findByUuid(uuid: UUID): Flow<Empleado>
    fun findByNombreContainsIgnoreCase(nombre: String): Flow<Empleado>
}