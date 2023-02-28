package resa.mario.services.departamento

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import resa.mario.models.Departamento
import resa.mario.repositories.departamento.DepartamentoCachedRepositoryImpl
import resa.mario.repositories.empleado.EmpleadoCachedRepositoryImpl

/**
 * Servicio encargado de realizar operaciones sobre Departamentos, haciendo uso de los repositorios necesarios
 *
 * @property repository
 * @property empleadosRepository
 */
@Service
class DepartamentoServiceImpl
@Autowired constructor(
    private val repository: DepartamentoCachedRepositoryImpl,
    private val empleadosRepository: EmpleadoCachedRepositoryImpl
) : DepartamentoService {

    override suspend fun findAll(): Flow<Departamento> {
        return repository.findAll()
    }

    override suspend fun findById(id: Long): Departamento? {
        return repository.findById(id) ?: throw Exception("No existe ese departamento")
    }

    override suspend fun save(entity: Departamento): Departamento {
        return repository.save(entity)
    }

    override suspend fun update(id: Long, entity: Departamento): Departamento? {
        val existe = repository.findById(id)

        existe?.let {
            return repository.update(id, entity)!!
        } ?: throw Exception("No se encontro ese departamento")
    }

    override suspend fun deleteById(id: Long): Departamento? {
        val existe = repository.findById(id)

        existe?.let {
            val empleados = empleadosRepository.findAll().toList().filter { it.departamentoId == existe.id }
            val count = empleados.size

            if (count == 0) {
                return repository.deleteById(id)
            } else throw Exception("No fue posible eliminar el departamento | $count empleados")
        } ?: throw Exception("No se encontro ese departamento")
    }
}