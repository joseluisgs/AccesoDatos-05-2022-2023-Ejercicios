package resa.mario.services.empleado

import kotlinx.coroutines.flow.Flow
import resa.mario.models.Empleado

interface EmpleadoService {
    suspend fun findAll(): Flow<Empleado>
    suspend fun findById(id: Long): Empleado?
    suspend fun save(entity: Empleado): Empleado
    suspend fun update(id: Long, entity: Empleado): Empleado?
    suspend fun deleteById(id: Long): Empleado?
}