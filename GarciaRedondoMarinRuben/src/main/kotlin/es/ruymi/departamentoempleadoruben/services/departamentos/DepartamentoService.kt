package es.ruymi.departamentoempleadoruben.services.departamentos

import es.ruymi.departamentoempleadoruben.models.Departamento
import kotlinx.coroutines.flow.Flow
import java.util.*

interface DepartamentoService {
    suspend fun findAll(): Flow<Departamento>
    suspend fun findById(id: Long): Departamento?
    suspend fun findByUuid(uuid: UUID): Departamento?
    suspend fun findByNombre(nombre: String): Departamento?
    suspend fun save(empleado: Departamento): Departamento
    suspend fun update(uuid: UUID, empleado: Departamento): Departamento?
    suspend fun delete(empleado: Departamento): Departamento?
    suspend fun deleteByUuid(uuid: UUID): Departamento?
    suspend fun deleteById(id: Long)
    suspend fun countAll(): Long
    suspend fun deleteAll()
}