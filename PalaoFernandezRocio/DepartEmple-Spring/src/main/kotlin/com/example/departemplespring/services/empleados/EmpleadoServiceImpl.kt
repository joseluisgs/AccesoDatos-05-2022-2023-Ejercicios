package com.example.departemplespring.services.empleados

import com.example.departemplespring.exceptions.EmpleadoNotFoundException
import com.example.departemplespring.models.Empleado
import com.example.departemplespring.repositories.empleados.EmpleadoRepository
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmpleadoServiceImpl
@Autowired constructor(
    private val repository: EmpleadoRepository
): EmpleadoService{

    override suspend fun findAll(): List<Empleado> {
        return repository.findAll().toList()
    }

    override suspend fun findById(id: Long): Empleado? {
        return repository.findById(id)
            ?: throw EmpleadoNotFoundException("No existe un empleado con el id: $id")
    }

    override suspend fun save(item: Empleado): Empleado {
        return repository.save(item)
    }

    override suspend fun update(id: Long, item: Empleado): Empleado? {
        val find = repository.findById(id)
        find?.let {
            val updated = it.copy(
                nombre = item.nombre,
                email = item.email,
                uuidDepartamento = item.uuidDepartamento,
//                rol = item.rol
            )
            repository.save(updated)
            return updated
        } ?: throw EmpleadoNotFoundException("No existe un empleado con el id: $id")
    }

    override suspend fun deleteById(id: Long): Empleado? {
        val find = repository.findById(id)
        find?.let {
            repository.delete(it)
            return it
        }?: throw EmpleadoNotFoundException("No existe un empleado con el id: $id")
    }

    override suspend fun findEmpleadosByDepartamentoUuid(departamentoUuid: UUID): List<Empleado> {
        return repository.findEmpleadosByUuidDepartamento(departamentoUuid).toList()
    }
}