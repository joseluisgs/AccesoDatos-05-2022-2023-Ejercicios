package es.dam.springrest.repositories.empleados

import es.dam.springrest.models.Empleado
import es.dam.springrest.repositories.ICachedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository

@Repository
class EmpleadosCachedRepository
    @Autowired constructor(
    private val empleadosRepository: EmpleadosRepository
): ICachedRepository<Empleado, Long> {
    override suspend fun findAll(): Flow<Empleado> = withContext(Dispatchers.IO) {
        return@withContext empleadosRepository.findAll()
    }

    @Cacheable("empleados")
    override suspend fun findById(id: Long): Empleado? = withContext(Dispatchers.IO) {
        return@withContext empleadosRepository.findById(id)
    }

    @CachePut("empleados")
    override suspend fun save(entity: Empleado): Empleado = withContext(Dispatchers.IO) {
        return@withContext empleadosRepository.save(entity)
    }

    @CachePut("empleados")
    override suspend fun update(id: Long, entity: Empleado): Empleado? = withContext(Dispatchers.IO) {
        var empleado = empleadosRepository.findById(id)
        empleado?.let {
            empleado = entity.copy(
                nombre = it.nombre,
                email = it.email,
                departamento_id = it.departamento_id,
                avatar = it.avatar
            )

            return@withContext empleadosRepository.save(empleado!!)
        }

        return@withContext null
    }

    @CacheEvict("empleados")
    override suspend fun delete(id: Long): Empleado? = withContext(Dispatchers.IO) {
        val empleado = empleadosRepository.findById(id)

        empleado?.let {
            empleadosRepository.delete(empleado)
        }

        return@withContext empleado
    }
}