package es.ruymi.departamentoempleadoruben.services.empleados

import es.ruymi.departamentoempleadoruben.config.websocket.ServerWebSocketConfig
import es.ruymi.departamentoempleadoruben.exceptions.EmpleadoNotFoundException
import es.ruymi.departamentoempleadoruben.models.Empleado
import es.ruymi.departamentoempleadoruben.repositories.empleados.EmpleadosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmpleadosServiceImpl
@Autowired constructor(
    private val empleadosRepository: EmpleadosRepository,
): EmpleadosService{
    override suspend fun findAll(): Flow<Empleado> {
        return empleadosRepository.findAll()
    }

    override suspend fun findById(id: Long): Empleado? {
        return empleadosRepository.findById(id) ?: throw EmpleadoNotFoundException(" No se ha encontrado el empleado con id: $id")
    }

    override suspend fun findByUuid(uuid: UUID): Empleado? {
        return empleadosRepository.findByUuid(uuid).firstOrNull()?: throw EmpleadoNotFoundException(" No se ha encontrado el empleado con uuid: $uuid")
    }

    override suspend fun findByNombre(nombre: String): Empleado? {
        return empleadosRepository.findByNombreContainsIgnoreCase(nombre).firstOrNull()?: throw EmpleadoNotFoundException(" No se ha encontrado el empleado con nombre: $nombre")
    }

    override suspend fun save(empleado: Empleado): Empleado {
        return empleadosRepository.save(empleado)
    }

    override suspend fun update(uuid: UUID, empleado: Empleado): Empleado? {
        val existe = empleadosRepository.findByUuid(uuid).firstOrNull()

        existe?.let {
            empleadosRepository.delete(existe)
            return empleadosRepository.save(empleado)
        } ?: throw EmpleadoNotFoundException(" No se ha encontrado el empleado con uuid: $uuid")

    }

    override suspend fun delete(empleado: Empleado): Empleado? {
        val existe = empleadosRepository.findByUuid(empleado.uuid).firstOrNull()

        existe?.let {
            empleadosRepository.delete(empleado)
            return empleado
        } ?: throw EmpleadoNotFoundException(" No se ha encontrado el empleado con uuid: ${empleado.uuid}")
    }

    override suspend fun deleteByUuid(uuid: UUID): Empleado? {
        val existe = empleadosRepository.findByUuid(uuid).firstOrNull()

        existe?.let {
            empleadosRepository.delete(existe)
            return existe
        } ?: throw EmpleadoNotFoundException(" No se ha encontrado el empleado con uuid: $uuid")

    }

    override suspend fun deleteById(id: Long) {
        val existe = empleadosRepository.findById(id)

        existe?.let {
            empleadosRepository.delete(existe)
        } ?: throw EmpleadoNotFoundException(" No se ha encontrado el empleado con id: $id")

    }

    override suspend fun countAll(): Long {
        return empleadosRepository.count()
    }

    override suspend fun deleteAll() {
        return empleadosRepository.deleteAll()
    }
}