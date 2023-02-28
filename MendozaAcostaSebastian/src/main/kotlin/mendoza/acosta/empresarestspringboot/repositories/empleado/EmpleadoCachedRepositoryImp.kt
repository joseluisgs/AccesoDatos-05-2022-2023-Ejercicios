package mendoza.acosta.empresarestspringboot.repositories.empleado

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import mendoza.acosta.empresarestspringboot.models.Empleado
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

private val logger = KotlinLogging.logger { }

@Repository
class EmpleadoCachedRepositoryImp
@Autowired constructor(
    private val repository: EmpleadoRepository
) : EmpleadoCachedRepository {

    init {
        logger.info { "Iniciando repositorio cacheado de empleado" }
    }

    override suspend fun findAll(): Flow<Empleado> = withContext(Dispatchers.IO) {
        logger.info { "Repositorio de empleados findAll()" }
        return@withContext repository.findAll()
    }

    @Cacheable("empleados")
    override suspend fun findByUuid(uuid: UUID): Empleado? = withContext(Dispatchers.IO) {
        logger.info { "Repositorio de empleados findById($uuid)" }
        return@withContext repository.findByUuid(uuid).firstOrNull()
    }

    override suspend fun findByNombre(nombre: String): Flow<Empleado> = withContext(Dispatchers.IO) {
        logger.info { "Repositorio de empleados findByNombre($nombre)" }
        return@withContext repository.findByNombreContainsIgnoreCase(nombre)
    }

    @CachePut("empleados")
    override suspend fun save(empleado: Empleado): Empleado = withContext(Dispatchers.IO) {
        logger.info { "Repositorio de empleados save(${empleado.nombre})" }
        val emp = empleado.copy(
            nombre = empleado.nombre,
            email = empleado.email,
        )
        return@withContext repository.save(emp)
    }

    @CachePut("empleados")
    override suspend fun update(uuid: UUID, empleado: Empleado): Empleado? = withContext(Dispatchers.IO) {
        logger.info { "Repositorio de empleado update($uuid, ${empleado.nombre})" }
        val emp = repository.findByUuid(uuid).firstOrNull()

        emp?.let {
            val updated = it.copy(
                uuid = it.uuid,
                nombre = empleado.nombre,
                email = empleado.email,
                salario = empleado.salario,
                updatedAt = LocalDateTime.now(),
                deleted = empleado.deleted
            )
            return@withContext repository.save(updated)
        }
        return@withContext null
    }

    @CacheEvict("empleados")
    override suspend fun deleteByUuid(uuid: UUID): Empleado? = withContext(Dispatchers.IO) {
        logger.info { "Repositorio de empleados deleteByUuid($uuid)" }
        val emp = repository.findByUuid(uuid).firstOrNull()
        emp?.let {
            repository.deleteById(it.id!!)
            return@withContext it
        }
        return@withContext null
    }

}