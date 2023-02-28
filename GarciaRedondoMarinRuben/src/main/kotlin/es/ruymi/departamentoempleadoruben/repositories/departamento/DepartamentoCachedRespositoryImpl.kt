package es.ruymi.departamentoempleadoruben.repositories.departamento

import es.ruymi.departamentoempleadoruben.models.Departamento
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
class DepartamentoCachedRespositoryImpl
@Autowired constructor(
    private val departamentoRepository: DepartamentoRepository
): DepartamentoCachedRepository {



    override suspend fun findAll(): Flow<Departamento> = withContext(Dispatchers.IO) {
        return@withContext departamentoRepository.findAll()
    }

    @Cacheable("departamento")
    override suspend fun findById(id: Long): Departamento? = withContext(Dispatchers.IO) {
        return@withContext departamentoRepository.findById(id)
    }

    @Cacheable("departamento")
    override suspend fun findByUuid(uuid: UUID): Departamento? = withContext(Dispatchers.IO){
        return@withContext departamentoRepository.findByUuid(uuid).firstOrNull()
    }

    override suspend fun findByNombre(nombre: String): Flow<Departamento> = withContext(Dispatchers.IO){
        return@withContext departamentoRepository.findByNombreContainsIgnoreCase(nombre)
    }

    @CachePut("departamento")
    override suspend fun save(departamento: Departamento): Departamento = withContext(Dispatchers.IO){
        return@withContext departamentoRepository.save(departamento)
    }

    @CachePut("departamento")
    override suspend fun update(uuid: UUID, departamento: Departamento): Departamento? = withContext(Dispatchers.IO){
        val res = departamentoRepository.findByUuid(uuid).firstOrNull()
        res?.let {
            val resUpdate = it.copy(
                uuid = it.uuid,
                nombre = departamento.nombre,
                presupuesto = departamento.presupuesto
            )
            return@let departamentoRepository.save(resUpdate)
        }
        return@withContext null
    }

    @CacheEvict("representantes")
    override suspend fun delete(departamento: Departamento): Departamento? = withContext(Dispatchers.IO){
        val res = departamentoRepository.findByUuid(departamento.uuid).firstOrNull()
        res?.let {
            departamentoRepository.deleteById(it.id!!)
            return@withContext it
        }
        return@withContext null
    }

    @CacheEvict("representantes")
    override suspend fun deleteByUuid(uuid: UUID): Departamento? = withContext(Dispatchers.IO){
        val res = departamentoRepository.findByUuid(uuid).firstOrNull()
        res?.let {
            departamentoRepository.deleteById(it.id!!)
            return@withContext it
        }
        return@withContext null
    }

    @CacheEvict("representantes")
    override suspend fun deleteById(id: Long) = withContext(Dispatchers.IO){
        departamentoRepository.deleteById(id)
    }

    override suspend fun countAll(): Long = withContext(Dispatchers.IO){
        return@withContext departamentoRepository.count()
    }

    override suspend fun deleteAll() = withContext(Dispatchers.IO){
        departamentoRepository.deleteAll()
    }
}