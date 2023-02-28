package es.dam.springrest.services.departamentos

import es.dam.springrest.exceptions.DepartamentoNotFoundException
import es.dam.springrest.models.Departamento
import es.dam.springrest.repositories.departamentos.DepartamentosCachedRepository
import es.dam.springrest.repositories.empleados.EmpleadosRepository
import es.dam.springrest.services.IService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DepartamentosService
    @Autowired constructor(
        private val departamentosCacheRepository: DepartamentosCachedRepository,
        private val empleadosRepository: EmpleadosRepository
    ): IService<Departamento, Long> {

    override suspend fun findAll(): Flow<Departamento> {
        return departamentosCacheRepository.findAll()
    }

    override suspend fun findById(id: Long): Departamento? {
        return departamentosCacheRepository.findById(id) ?:
        throw DepartamentoNotFoundException("Departamento con id $id no encontrado.")
    }

    override suspend fun save(entity: Departamento): Departamento {
        return departamentosCacheRepository.save(entity)
    }

    override suspend fun update(id: Long, entity: Departamento): Departamento? {
        val departamento = departamentosCacheRepository.findById(id)

        departamento?.let {
            return departamentosCacheRepository.update(id, entity)
        } ?:
        throw DepartamentoNotFoundException("Departamento con id $id no encontrado.")
    }

    override suspend fun delete(id: Long): Departamento? {
        val departamento = departamentosCacheRepository.findById(id)

        require(empleadosRepository.findAll().toList().none { it.departamento_id == departamento?.id }) {
            "No se puede eliminar un departamento_id con empleados."
        }

        return departamentosCacheRepository.delete(id) ?:
        throw DepartamentoNotFoundException("Departamento con id $id no encontrado.")
    }
}