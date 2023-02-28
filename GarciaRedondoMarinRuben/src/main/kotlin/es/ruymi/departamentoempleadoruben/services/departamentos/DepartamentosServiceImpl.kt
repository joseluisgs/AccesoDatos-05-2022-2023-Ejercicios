package es.ruymi.departamentoempleadoruben.services.departamentos

import es.ruymi.departamentoempleadoruben.exceptions.DepartamentoNotFoundException
import es.ruymi.departamentoempleadoruben.models.Departamento
import es.ruymi.departamentoempleadoruben.repositories.departamento.DepartamentoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class DepartamentosServiceImpl
@Autowired constructor(
    private val departamentoRepository: DepartamentoRepository,
): DepartamentoService{
    override suspend fun findAll(): Flow<Departamento> {
        return departamentoRepository.findAll()
    }

    override suspend fun findById(id: Long): Departamento? {
        return departamentoRepository.findById(id) ?: throw DepartamentoNotFoundException(" No se ha encontrado el empleado con id: $id")
    }

    override suspend fun findByUuid(uuid: UUID): Departamento? {
        return departamentoRepository.findByUuid(uuid).firstOrNull()?: throw DepartamentoNotFoundException(" No se ha encontrado el empleado con uuid: $uuid")
    }

    override suspend fun findByNombre(nombre: String): Departamento? {
        return departamentoRepository.findByNombreContainsIgnoreCase(nombre).firstOrNull()?: throw DepartamentoNotFoundException(" No se ha encontrado el empleado con nombre: $nombre")
    }

    override suspend fun save(empleado: Departamento): Departamento {
        return departamentoRepository.save(empleado)
    }

    override suspend fun update(uuid: UUID, empleado: Departamento): Departamento? {
        val existe = departamentoRepository.findByUuid(uuid).firstOrNull()

        existe?.let {
            departamentoRepository.delete(existe)
            return departamentoRepository.save(empleado)
        } ?: throw DepartamentoNotFoundException(" No se ha encontrado el empleado con uuid: $uuid")

    }

    override suspend fun delete(empleado: Departamento): Departamento? {
        val existe = departamentoRepository.findByUuid(empleado.uuid).firstOrNull()

        existe?.let {
            departamentoRepository.delete(empleado)
            return empleado
        } ?: throw DepartamentoNotFoundException(" No se ha encontrado el empleado con uuid: ${empleado.uuid}")
    }

    override suspend fun deleteByUuid(uuid: UUID): Departamento? {
        val existe = departamentoRepository.findByUuid(uuid).firstOrNull()

        existe?.let {
            departamentoRepository.delete(existe)
            return existe
        } ?: throw DepartamentoNotFoundException(" No se ha encontrado el empleado con uuid: $uuid")

    }

    override suspend fun deleteById(id: Long) {
        val existe = departamentoRepository.findById(id)

        existe?.let {
            departamentoRepository.delete(existe)
        } ?: throw DepartamentoNotFoundException(" No se ha encontrado el empleado con id: $id")

    }

    override suspend fun countAll(): Long {
        return departamentoRepository.count()
    }

    override suspend fun deleteAll() {
        return departamentoRepository.deleteAll()
    }
}