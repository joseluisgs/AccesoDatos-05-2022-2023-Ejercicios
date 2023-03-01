package com.example.apiempleado.repository.departamento

import com.example.apiempleado.model.Departamento
import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository

@Repository
class DepartamentoCacheRepositoryImpl
@Autowired constructor(
    private val repository: DepartamentoRepository,
) : DepartamentoCacheRepository {
    override suspend fun findAll(): Flow<Departamento> {
        return repository.findAll()
    }

    @Cacheable("departamentos")
    override suspend fun findById(id: Long): Departamento? {
        return repository.findById(id)
    }

    @CachePut("departamentos")
    override suspend fun save(departamento: Departamento): Departamento {
        return repository.save(departamento)
    }

    @CachePut("departamentos")
    override suspend fun update(id: Long, departamento: Departamento): Departamento? {
        val exist = repository.findById(id)
        exist?.let {
            val update = it.copy(
                uuid = it.uuid,
                name = departamento.name
            )
            return repository.save(update)
        }
        return null
    }

    @CacheEvict("departamentos")
    override suspend fun delete(departamento: Departamento): Boolean {
        val exist = repository.findById(departamento.id!!)
        exist?.let {
            repository.deleteById(it.id!!)
            return true
        }
        return false
    }

    @CacheEvict("departamentos")
    override suspend fun deleteById(id: Long) {
        repository.deleteById(id)
    }

    override suspend fun countAll(): Long {
        return repository.count()
    }

    @CacheEvict("departamentos", allEntries = true)
    override suspend fun deleteAll() {
        repository.deleteAll()
    }
}