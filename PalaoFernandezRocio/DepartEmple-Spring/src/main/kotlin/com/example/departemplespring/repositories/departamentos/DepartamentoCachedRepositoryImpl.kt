package com.example.departemplespring.repositories.departamentos

import com.example.departemplespring.models.Departamento
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

@Repository
class DepartamentoCachedRepositoryImpl
@Autowired constructor(
    private val repository: DepartamentoRepository
) : DepartamentoCachedRepository{

    override suspend fun findAll(): Flow<Departamento> = withContext(Dispatchers.IO){
        return@withContext repository.findAll()
    }

    @Cacheable("departamentos")
    override suspend fun findById(id: Long): Departamento? = withContext(Dispatchers.IO){
        return@withContext repository.findById(id)
    }

    @Cacheable("departamentos")
    override suspend fun findByUuid(uuid: UUID): Departamento? = withContext(Dispatchers.IO){
        return@withContext repository.findDepartamentoByUuid(uuid).firstOrNull()
    }

    @CachePut("departamentos")
    override suspend fun save(item: Departamento): Departamento = withContext(Dispatchers.IO){
        return@withContext repository.save(item)
    }

    @CachePut("departamentos")
    override suspend fun update(id: Long, item: Departamento): Departamento? = withContext(Dispatchers.IO){
        val departamento = repository.findById(id)
        departamento?.let {
            val updated = it.copy(
                uuid = it.uuid,
                nombre = item.nombre,
                presupuesto = item.presupuesto
            )
            return@withContext repository.save(updated)
        }
        return@withContext null
    }

    @CacheEvict("departamentos")
    override suspend fun deleteById(id: Long): Departamento? = withContext(Dispatchers.IO){
        val find = repository.findById(id)
        find?.let {
            repository.deleteById(it.id!!)
            return@withContext it
        }
        return@withContext null
    }
}