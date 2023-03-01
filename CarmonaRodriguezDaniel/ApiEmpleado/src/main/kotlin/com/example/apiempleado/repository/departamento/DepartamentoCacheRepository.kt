package com.example.apiempleado.repository.departamento

import com.example.apiempleado.model.Departamento
import kotlinx.coroutines.flow.Flow

interface DepartamentoCacheRepository {
    suspend fun findAll(): Flow<Departamento>
    suspend fun findById(id: Long): Departamento?
    suspend fun save(departamento: Departamento): Departamento
    suspend fun update(id: Long, departamento: Departamento): Departamento?
    suspend fun delete(departamento: Departamento): Boolean
    suspend fun deleteById(id: Long)
    suspend fun countAll(): Long
    suspend fun deleteAll()
}