package es.dam.springrest.services.empleados

import es.dam.springrest.exceptions.EmpleadoNotFoundException
import es.dam.springrest.models.Empleado
import es.dam.springrest.repositories.empleados.EmpleadosCachedRepository
import es.dam.springrest.services.IService
import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EmpleadosService
    @Autowired constructor(
        private val empleadosCachedRepository: EmpleadosCachedRepository
    ): IService<Empleado, Long> {

    override suspend fun findAll(): Flow<Empleado> {
        return empleadosCachedRepository.findAll()
    }

    override suspend fun findById(id: Long): Empleado? {
        return empleadosCachedRepository.findById(id) ?:
        throw EmpleadoNotFoundException("Empleado con id $id no encontrado.")
    }

    override suspend fun save(entity: Empleado): Empleado {
        return empleadosCachedRepository.save(entity)
    }

    override suspend fun update(id: Long, entity: Empleado): Empleado? {
        val empleado = empleadosCachedRepository.findById(id)

        empleado?.let {
            return empleadosCachedRepository.update(id, entity)
        } ?:
        throw EmpleadoNotFoundException("Empleado con id $id no encontrado.")
    }

    override suspend fun delete(id: Long): Empleado? {
        val empleado = empleadosCachedRepository.findById(id)

        empleado?.let {
            return empleadosCachedRepository.delete(id)
        } ?:
        throw EmpleadoNotFoundException("Empleado con id $id no encontrado.")
    }
}