package mireya.sanchez.apispring.services.departamento

import kotlinx.coroutines.flow.Flow
import mireya.sanchez.apispring.models.Departamento

interface IDepartamentosService  {
    suspend fun findAll(): Flow<Departamento>
    suspend fun findById(id: Long): Departamento?
    suspend fun save(entity: Departamento): Departamento
    suspend fun update(id: Long, entity: Departamento): Departamento?
    suspend fun deleteById(id: Long): Departamento?
}