package com.example.departemplespring.repositories.departamentos

import com.example.departemplespring.models.Departamento
import kotlinx.coroutines.flow.Flow
import java.util.*

interface DepartamentoCachedRepository {
    suspend fun findAll(): Flow<Departamento>
    suspend fun findById(id: Long): Departamento?
    suspend fun findByUuid(uuid: UUID): Departamento?
    suspend fun save(item: Departamento): Departamento
    suspend fun update(id: Long, item:Departamento): Departamento?
    suspend fun deleteById(id: Long): Departamento?
}