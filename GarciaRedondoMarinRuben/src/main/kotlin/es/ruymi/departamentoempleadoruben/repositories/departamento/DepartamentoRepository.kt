package es.ruymi.departamentoempleadoruben.repositories.departamento

import es.ruymi.departamentoempleadoruben.models.Departamento
import es.ruymi.departamentoempleadoruben.models.Empleado
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DepartamentoRepository: CoroutineCrudRepository<Departamento, Long> {
    fun findByUuid(uuid: UUID): Flow<Departamento>
    fun findByNombreContainsIgnoreCase(nombre: String): Flow<Departamento>
}