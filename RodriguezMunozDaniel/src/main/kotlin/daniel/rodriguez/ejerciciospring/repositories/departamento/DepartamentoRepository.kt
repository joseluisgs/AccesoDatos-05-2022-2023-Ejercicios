package daniel.rodriguez.ejerciciospring.repositories.departamento

import daniel.rodriguez.ejerciciospring.models.Departamento
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DepartamentoRepository : CoroutineCrudRepository<Departamento, Long> {
    fun findByUuid(uuid: UUID): Flow<Departamento>
}