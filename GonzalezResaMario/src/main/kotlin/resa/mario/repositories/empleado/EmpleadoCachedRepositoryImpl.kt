package resa.mario.repositories.empleado

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository
import resa.mario.models.Empleado

private val log = KotlinLogging.logger {}

/**
 * Repositorio encargado de realizar operaciones con Empleados
 *
 * @property repository
 */
@Repository
class EmpleadoCachedRepositoryImpl
@Autowired constructor(
    private val repository: EmpleadoRepository
) : EmpleadoCachedRepository {

    override suspend fun findAll(): Flow<Empleado> = withContext(Dispatchers.IO) {
        log.info { "Obteniendo todos los empleados" }

        return@withContext repository.findAll()
    }

    @Cacheable("empleados")
    override suspend fun findById(id: Long): Empleado? = withContext(Dispatchers.IO) {
        log.info { "Obteniendo empleado con id: $id" }

        return@withContext repository.findById(id)
    }

    @CachePut("empleados")
    override suspend fun save(entity: Empleado): Empleado = withContext(Dispatchers.IO) {
        log.info { "Almacenando empleado: $entity" }

        return@withContext repository.save(entity)
    }

    @CachePut("empleados")
    override suspend fun update(id: Long, entity: Empleado): Empleado? = withContext(Dispatchers.IO) {
        log.info { "Actualizando empleado con id: $id" }

        var entityDB = repository.findById(id)

        if (entityDB != null) {
            entityDB = entity.copy(id = id)
            entityDB = repository.save(entityDB)
        }

        return@withContext entityDB
    }

    @CacheEvict("empleados")
    override suspend fun deleteById(id: Long): Empleado? = withContext(Dispatchers.IO) {
        log.info { "Eliminando empleado con id: $id " }

        val entityDB = repository.findById(id)

        if (entityDB != null) {
            repository.delete(entityDB)
        }

        return@withContext entityDB
    }
}