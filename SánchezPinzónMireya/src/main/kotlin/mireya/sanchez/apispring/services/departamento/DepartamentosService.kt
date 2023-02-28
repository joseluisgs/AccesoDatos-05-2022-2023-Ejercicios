package mireya.sanchez.apispring.services.departamento

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import mireya.sanchez.apispring.exceptions.DepartamentoConflictIntegrityException
import mireya.sanchez.apispring.exceptions.DepartamentoNotFoundException
import mireya.sanchez.apispring.models.Departamento
import mireya.sanchez.apispring.repositories.departamento.DepartamentosCachedRepository
import mireya.sanchez.apispring.repositories.empleado.EmpleadosCachedRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DepartamentosService
@Autowired constructor(
    private val repository: DepartamentosCachedRepository,
    private val empleadosRepository: EmpleadosCachedRepository
) : IDepartamentosService {

    override suspend fun findAll(): Flow<Departamento> {
        return repository.findAll()
    }

    override suspend fun findById(id: Long): Departamento? {
        return repository.findById(id) ?: throw DepartamentoNotFoundException("Departamento no encontrado")
    }

    override suspend fun save(entity: Departamento): Departamento {
        return repository.save(entity)
    }

    override suspend fun update(id: Long, entity: Departamento): Departamento? {
        val existe = repository.findById(id)

        existe?.let {
            return repository.update(id, entity)!!
        } ?: throw DepartamentoNotFoundException("Departamento no encontrado")
    }

    override suspend fun deleteById(id: Long): Departamento? {
        val existe = repository.findById(id)

        existe?.let {
            val empleados = empleadosRepository.findAll().toList().filter { it.departamentoId == existe.id }
            val numEmpleados = empleados.size

            if (numEmpleados == 0) {
                return repository.deleteById(id)
            } else throw DepartamentoConflictIntegrityException("No se puede borrar. Tiene $numEmpleados empleados asociados.")
        } ?: throw DepartamentoNotFoundException("Departamento no encontrado")
    }
}