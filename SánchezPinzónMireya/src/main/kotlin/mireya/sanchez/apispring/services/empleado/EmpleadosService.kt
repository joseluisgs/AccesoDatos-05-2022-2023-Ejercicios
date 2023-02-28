package mireya.sanchez.apispring.services.empleado

import kotlinx.coroutines.flow.Flow
import mireya.sanchez.apispring.exceptions.EmpleadoNotFoundException
import mireya.sanchez.apispring.models.Empleado
import mireya.sanchez.apispring.repositories.departamento.DepartamentosCachedRepository
import mireya.sanchez.apispring.repositories.empleado.EmpleadosCachedRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EmpleadosService
@Autowired constructor(
    private val repository: EmpleadosCachedRepository,
    private val departamentoRepository: DepartamentosCachedRepository
) : IEmpleadoService {

    override suspend fun findAll(): Flow<Empleado> {
        return repository.findAll()
    }

    override suspend fun findById(id: Long): Empleado? {
        return repository.findById(id) ?: throw EmpleadoNotFoundException("Empleado no encontrado")
    }

    override suspend fun save(entity: Empleado): Empleado {
        if (entity.departamentoId != null) {
            val existe = departamentoRepository.findById(entity.departamentoId!!)
            if (existe == null) {
                System.err.println("Not found")
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
                    entity.departamentoId = existe.departamentoId
                }
            }
            return repository.update(id, entity)!!
        } ?: throw EmpleadoNotFoundException("Empleado no encontrado")
    }

    override suspend fun deleteById(id: Long): Empleado? {
        val existe = repository.findById(id)

        existe?.let {
            return repository.deleteById(id)!!
        } ?: throw EmpleadoNotFoundException("Empleado no encontrado")
    }
}
