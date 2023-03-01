package daniel.rodriguez.ejerciciospring.repositories.empleado

import daniel.rodriguez.ejerciciospring.exception.EmpleadoExceptionBadRequest
import daniel.rodriguez.ejerciciospring.models.Empleado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository
import java.util.*
import kotlin.jvm.Throws

@Repository
class EmpleadoRepositoryCached
@Autowired constructor(
    private val repo: EmpleadoRepository
) : IEmpleadoRepositoryCached {
    override suspend fun findAll(): Flow<Empleado> = withContext(Dispatchers.IO) {
        repo.findAll()
    }

    @Cacheable("workers")
    override suspend fun findById(id: Long): Empleado? = withContext(Dispatchers.IO) {
        repo.findById(id)
    }

    override suspend fun findByUUID(id: UUID): Empleado? = withContext(Dispatchers.IO) {
        repo.findByUuid(id).firstOrNull()
    }

    @Throws(EmpleadoExceptionBadRequest::class)
    @CachePut("workers")
    override suspend fun save(entity: Empleado): Empleado = withContext(Dispatchers.IO) {
        repo.save(entity)
    }

    @Throws(EmpleadoExceptionBadRequest::class)
    @CacheEvict("workers")
    override suspend fun delete(id: Long): Empleado? = withContext(Dispatchers.IO) {
        val res = repo.findById(id) ?: return@withContext null
        repo.deleteById(id)
        res
    }
}