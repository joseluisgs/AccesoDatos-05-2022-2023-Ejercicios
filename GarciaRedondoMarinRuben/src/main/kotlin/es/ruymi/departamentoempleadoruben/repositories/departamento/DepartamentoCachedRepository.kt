package es.ruymi.departamentoempleadoruben.repositories.departamento

import es.ruymi.departamentoempleadoruben.models.Departamento
import kotlinx.coroutines.flow.Flow
import java.util.*


interface DepartamentoCachedRepository {
    suspend fun findAll(): Flow<Departamento>
    suspend fun findById(id: Long): Departamento?
    suspend fun findByUuid(uuid: UUID): Departamento?
    suspend fun findByNombre(nombre: String): Flow<Departamento>
    suspend fun save(departamento: Departamento): Departamento
    suspend fun update(uuid: UUID, departamento: Departamento): Departamento?
    suspend fun delete(departamento: Departamento): Departamento?
    suspend fun deleteByUuid(uuid: UUID): Departamento?
    suspend fun deleteById(id: Long)
    suspend fun countAll(): Long
    suspend fun deleteAll()
}