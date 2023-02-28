package resa.mario.services.departamento

import kotlinx.coroutines.flow.Flow
import resa.mario.models.Departamento

interface DepartamentoService {
    suspend fun findAll(): Flow<Departamento>
    suspend fun findById(id: Long): Departamento?
    suspend fun save(entity: Departamento): Departamento
    suspend fun update(id: Long, entity: Departamento): Departamento?
    suspend fun deleteById(id: Long): Departamento?
}