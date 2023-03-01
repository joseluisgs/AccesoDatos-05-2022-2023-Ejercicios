package com.example.apiempleado.repository.empleado

import com.example.apiempleado.model.Empleado
import kotlinx.coroutines.flow.Flow

interface EmpleadoCacheRepository {
    suspend fun findAll(): Flow<Empleado>
    suspend fun findById(id: Long): Empleado?
    suspend fun save(empleado: Empleado): Empleado
    suspend fun update(id: Long, empleado: Empleado): Empleado?
    suspend fun delete(empleado: Empleado): Boolean
    suspend fun deleteById(id: Long)
    suspend fun countAll(): Long
    suspend fun deleteAll()
}