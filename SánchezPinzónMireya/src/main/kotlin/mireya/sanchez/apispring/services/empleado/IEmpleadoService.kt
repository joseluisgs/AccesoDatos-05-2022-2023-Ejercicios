package mireya.sanchez.apispring.services.empleado

import kotlinx.coroutines.flow.Flow
import mireya.sanchez.apispring.models.Empleado

interface IEmpleadoService{
    suspend fun findAll(): Flow<Empleado>
    suspend fun findById(id: Long): Empleado?
    suspend fun save(entity: Empleado): Empleado
    suspend fun update(id: Long, entity: Empleado): Empleado?
    suspend fun deleteById(id: Long): Empleado?
}