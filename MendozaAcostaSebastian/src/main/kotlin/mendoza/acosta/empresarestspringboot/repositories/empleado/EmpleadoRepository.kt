package mendoza.acosta.empresarestspringboot.repositories.empleado

import kotlinx.coroutines.flow.Flow
import mendoza.acosta.empresarestspringboot.models.Empleado
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface EmpleadoRepository : CoroutineCrudRepository<Empleado, Long> {
    fun findByUuid(uuid: UUID): Flow<Empleado>
    fun findByNombreContainsIgnoreCase(nombre: String): Flow<Empleado>
}