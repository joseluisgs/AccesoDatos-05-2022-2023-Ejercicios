package es.ruymi.departamentoempleadoruben.repositories.empleados

import es.ruymi.departamentoempleadoruben.models.Empleado
import kotlinx.coroutines.flow.Flow
import java.util.UUID


interface EmpleadosCachedRepository {
    suspend fun findAll(): Flow<Empleado>
    suspend fun findById(id: Long): Empleado?
    suspend fun findByUuid(uuid: UUID): Empleado?
    suspend fun findByNombre(nombre: String): Flow<Empleado>
    suspend fun save(empleado: Empleado): Empleado
    suspend fun update(uuid: UUID, empleado: Empleado): Empleado?
    suspend fun delete(empleado: Empleado): Empleado?
    suspend fun deleteByUuid(uuid: UUID): Empleado?
    suspend fun deleteById(id: Long)
    suspend fun countAll(): Long
    suspend fun deleteAll()
}