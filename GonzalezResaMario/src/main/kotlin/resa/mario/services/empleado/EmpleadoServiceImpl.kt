package resa.mario.services.empleado

import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import resa.mario.models.Empleado
import resa.mario.repositories.departamento.DepartamentoCachedRepositoryImpl
import resa.mario.repositories.empleado.EmpleadoCachedRepositoryImpl

/**
 * Servicio encargado de realizar operaciones sobre Empleados, haciendo uso de los repositorios necesarios
 *
 * @property repository
 * @property departamentoRepository
 */
@Service
class EmpleadoServiceImpl
@Autowired constructor(
    private val repository: EmpleadoCachedRepositoryImpl,
    private val departamentoRepository: DepartamentoCachedRepositoryImpl
) : EmpleadoService {

    override suspend fun findAll(): Flow<Empleado> {
        return repository.findAll()
    }

    override suspend fun findById(id: Long): Empleado? {
        return repository.findById(id) ?: throw Exception("No existe ese empleado")
    }

    override suspend fun save(entity: Empleado): Empleado {
        if (entity.departamentoId != null) {
            val existe = departamentoRepository.findById(entity.departamentoId!!)
            if (existe == null) {
                System.err.println("Departamento not found")
                entity.departamentoId = null
            }
        }
        return repository.save(entity)
    }

    override suspend fun update(id: Long, entity: Empleado): Empleado? {
        val existe = repository.findById(id)

        existe?.let {
            if (entity.departamentoId != null) {
                val existe2 = departamentoRepository.findById(entity.departamentoId!!)
                if (existe2 == null) {
                    System.err.println("Departamento not found, imposible actualizar ese campo")
                    entity.departamentoId = existe.departamentoId
                }
            }
            return repository.update(id, entity)!!
        } ?: throw Exception("No se encontro ese empleado")
    }

    override suspend fun deleteById(id: Long): Empleado? {
        val existe = repository.findById(id)

        existe?.let {
            return repository.deleteById(id)!!
        } ?: throw Exception("No se encontro ese empleado")
    }
}