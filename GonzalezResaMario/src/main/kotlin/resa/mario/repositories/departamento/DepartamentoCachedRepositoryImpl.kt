package resa.mario.repositories.departamento

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import resa.mario.models.Departamento
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository

private val log = KotlinLogging.logger {}

/**
 * Repositorio que realiza operaciones con Departamentos
 *
 * @property repository
 */
@Repository
class DepartamentoCachedRepositoryImpl
@Autowired constructor(
    private val repository: DepartamentoRepository
) : DepartamentoCachedRepository {

    override suspend fun findAll(): Flow<Departamento> = withContext(Dispatchers.IO) {
        log.info { "Obteniendo todos los departamentos" }

        return@withContext repository.findAll()
    }

    @Cacheable("departamentos")
    override suspend fun findById(id: Long): Departamento? = withContext(Dispatchers.IO) {
        log.info { "Obteniendo departamento con id: $id" }

        return@withContext repository.findById(id)
    }

    @CachePut("departamentos")
    override suspend fun save(entity: Departamento): Departamento = withContext(Dispatchers.IO) {
        log.info { "Almacenando departamento: $entity" }

        return@withContext repository.save(entity)
    }

    // IMPORTANTE, PARA EVITAR QUE CUANDO SE ACTUALICE EL OBJETO CON UN NUEVO ID Y DEJANDO EL ANTIGUO:
    // SE DEBERA CAMBIAR EL ID DEL ENTITY ENVIADO, EN ESTE CASO ES NULL, O BIEN HACER UN COPY DEL ELEMENTO
    @CachePut("departamentos")
    override suspend fun update(id: Long, entity: Departamento): Departamento? = withContext(Dispatchers.IO) {
        log.info { "Actualizando departamento con id: $id" }

        var entityDB = repository.findById(id)

        if (entityDB != null) {
            entityDB = entity.copy(id = id)
            entityDB = repository.save(entityDB)
        }

        return@withContext entityDB
    }

    @CacheEvict("departamentos")
    override suspend fun deleteById(id: Long): Departamento? = withContext(Dispatchers.IO) {
        log.info { "Eliminando departamento con id: $id " }

        val entityDB = repository.findById(id)

        if (entityDB != null) {
            repository.delete(entityDB)
        }

        return@withContext entityDB
    }
}