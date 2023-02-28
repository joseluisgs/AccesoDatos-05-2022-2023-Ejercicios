package es.dam.springrest.repositories.departamentos

import es.dam.springrest.models.Departamento
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
class DepartamentosCachedRepository
    @Autowired constructor(
        private val departamentosRepository: DepartamentosRepository
    ): ICachedRepository<Departamento, Long> {

    override suspend fun findAll(): Flow<Departamento> = withContext(Dispatchers.IO) {
        return@withContext departamentosRepository.findAll()
    }

    @Cacheable("departamentos")
    override suspend fun findById(id: Long): Departamento? = withContext(Dispatchers.IO) {
        return@withContext departamentosRepository.findById(id)
    }

    @CachePut("departamentos")
    override suspend fun save(entity: Departamento): Departamento = withContext(Dispatchers.IO) {
        return@withContext departamentosRepository.save(entity)
    }

    @CachePut("departamentos")
    override suspend fun update(id: Long, entity: Departamento): Departamento? = withContext(Dispatchers.IO) {
        var departamento = departamentosRepository.findById(id)
        departamento?.let {
            departamento = entity.copy(
                nombre = it.nombre,
                presupuesto = it.presupuesto
            )

            return@withContext departamentosRepository.save(departamento!!)
        }

        return@withContext null
    }

    @CacheEvict("departamentos")
    override suspend fun delete(id: Long): Departamento? = withContext(Dispatchers.IO) {
        val departamento = departamentosRepository.findById(id)

        departamento?.let {
            departamentosRepository.delete(departamento)
        }

        return@withContext departamento
    }

}