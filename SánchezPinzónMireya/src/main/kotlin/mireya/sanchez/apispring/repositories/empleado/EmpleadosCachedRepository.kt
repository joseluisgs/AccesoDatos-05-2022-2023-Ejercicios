package mireya.sanchez.apispring.repositories.empleado

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import mireya.sanchez.apispring.models.Empleado
import mireya.sanchez.apispring.repositories.CRUDRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository


@Repository
class EmpleadosCachedRepository
@Autowired constructor(
    private val repository: EmpleadosRepository
) : CRUDRepository<Empleado, Long> {

    override suspend fun findAll(): Flow<Empleado> = withContext(Dispatchers.IO) {
        return@withContext repository.findAll()
    }

    @Cacheable("empleados")
    override suspend fun findById(id: Long): Empleado? = withContext(Dispatchers.IO) {
        return@withContext repository.findById(id)
    }

    @CachePut("empleados")
    override suspend fun save(entity: Empleado): Empleado = withContext(Dispatchers.IO) {
        return@withContext repository.save(entity)
    }

    @CachePut("empleados")
    override suspend fun update(id: Long, entity: Empleado): Empleado? = withContext(Dispatchers.IO) {
        var entityDB = repository.findById(id)

        if (entityDB != null) {
            entityDB = entity.copy(id = id)
            entityDB = repository.save(entityDB)
        }

        return@withContext entityDB
    }

    @CacheEvict("empleados")
    override suspend fun deleteById(id: Long): Empleado? = withContext(Dispatchers.IO) {
        val entityDB = repository.findById(id)

        if (entityDB != null) {
            repository.delete(entityDB)
        }

        return@withContext entityDB
    }
}