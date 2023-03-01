package com.example.apiempleado.repository.empleado

import com.example.apiempleado.model.Empleado
import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class EmpleadoCacheRepositoryImpl
@Autowired constructor(
    private val repository: EmpleadoRepository,
) : EmpleadoCacheRepository {
    override suspend fun findAll(): Flow<Empleado> {
        return repository.findAll()
    }

    @Cacheable("empleados")
    override suspend fun findById(id: Long): Empleado? {
        return repository.findById(id)
    }

    @CachePut("empleados")
    override suspend fun save(empleado: Empleado): Empleado {
        return repository.save(empleado)
    }

    @CachePut("empleados")
    override suspend fun update(id: Long, empleado: Empleado): Empleado? {

        val exist = repository.findById(id)
        exist?.let {
            val update = it.copy(
                uuid = it.uuid,
                name = empleado.name,
                available = empleado.available
            )
            return repository.save(update)
        }
        return null
    }

    @CacheEvict("empleados")
    override suspend fun delete(empleado: Empleado): Boolean {
        val exist = repository.findById(empleado.id!!)
        exist?.let {
            repository.deleteById(it.id!!)
            return true
        }
        return false
    }

    @CacheEvict("empleados")
    override suspend fun deleteById(id: Long) {
        repository.deleteById(id)
    }

    override suspend fun countAll(): Long {
        return repository.count()
    }

    @CacheEvict("empleados", allEntries = true)
    override suspend fun deleteAll() {
        repository.deleteAll()
    }

}