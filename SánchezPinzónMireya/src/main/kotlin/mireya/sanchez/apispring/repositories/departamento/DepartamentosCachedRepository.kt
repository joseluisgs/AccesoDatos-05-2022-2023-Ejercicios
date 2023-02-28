package mireya.sanchez.apispring.repositories.departamento

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import mireya.sanchez.apispring.models.Departamento
import mireya.sanchez.apispring.repositories.CRUDRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository


@Repository
class DepartamentosCachedRepository
@Autowired constructor(
    private val repository: DepartamentosRepository
) : CRUDRepository<Departamento, Long> {

    override suspend fun findAll(): Flow<Departamento> = withContext(Dispatchers.IO) {
        return@withContext repository.findAll()
    }

    @Cacheable("departamentos")
    override suspend fun findById(id: Long): Departamento? = withContext(Dispatchers.IO) {
        return@withContext repository.findById(id)
    }

    @CachePut("departamentos")
    override suspend fun save(entity: Departamento): Departamento = withContext(Dispatchers.IO) {
        return@withContext repository.save(entity)
    }

    @CachePut("departamentos")
    override suspend fun update(id: Long, entity: Departamento): Departamento? = withContext(Dispatchers.IO) {
        var exist = repository.findById(id)

        //TODO: pensarlo mejor
        if (exist != null) {
            exist = entity.copy(id = id)
            exist = repository.save(exist)
        }

        return@withContext exist
    }

    @CacheEvict("departamentos")
    override suspend fun deleteById(id: Long): Departamento? = withContext(Dispatchers.IO) {
        val entityDB = repository.findById(id)

        if (entityDB != null) {
            repository.delete(entityDB)
        }

        return@withContext entityDB
    }

}