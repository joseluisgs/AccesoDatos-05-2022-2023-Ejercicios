package mendoza.acosta.empresarestspringboot.service.empleado

import kotlinx.coroutines.flow.Flow
import mendoza.acosta.empresarestspringboot.exception.EmpleadoNotFoundException
import mendoza.acosta.empresarestspringboot.models.Empleado
import mendoza.acosta.empresarestspringboot.repositories.empleado.EmpleadoCachedRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

private val logger = KotlinLogging.logger { }

@Service
class EmpleadoServiceImp
@Autowired constructor(
    private val repository: EmpleadoCachedRepository
) : EmpleadoService {

    init {
        logger.debug { "Iniciando servicio de empleado" }
    }

    override suspend fun findAll(): Flow<Empleado> {
        logger.debug { "Servicio de empleados findAll()" }
        return repository.findAll()
    }

    override suspend fun findByUuid(uuid: UUID): Empleado {
        logger.debug { "Servicio de empleados findByUuid($uuid)" }
        return repository.findByUuid(uuid)
            ?: throw EmpleadoNotFoundException("No se ha encontrado empleado con uuid $uuid")
    }

    override suspend fun findByNombre(nombre: String): Flow<Empleado> {
        logger.debug { "Servicio de empleado findByNombre($nombre)" }
        return repository.findByNombre(nombre)
    }

    override suspend fun save(empleado: Empleado): Empleado {
        logger.debug { "Servicio de save(${empleado.nombre})" }
        return repository.save(empleado)
    }

    override suspend fun update(uuid: UUID, empleado: Empleado): Empleado {
        logger.debug { "Servicio de empleado update(${empleado.nombre})" }
        val existe = repository.findByUuid(uuid)
        existe?.let {
            return repository.update(uuid, empleado)!!
        } ?: throw EmpleadoNotFoundException("No se ha encontrado empleado empleado con uuid $uuid")
    }

    override suspend fun deleteByUuid(uuid: UUID): Empleado {
        logger.debug { "Servicio de empleado deleteByUuid con uuid $uuid" }
        val existe = repository.findByUuid(uuid)
        existe?.let {
            return repository.deleteByUuid(uuid)!!
        } ?: throw EmpleadoNotFoundException("No se ha encontrado empleado con uuid: $uuid")
    }

}