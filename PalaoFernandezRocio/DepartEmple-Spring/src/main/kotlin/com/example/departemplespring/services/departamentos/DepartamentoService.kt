package com.example.departemplespring.services.departamentos

import com.example.departemplespring.models.Departamento
import java.util.*

interface DepartamentoService {
    suspend fun findAll(): List<Departamento>
    suspend fun findById(id: Long): Departamento?
    suspend fun findByUuid(uuid: UUID): Departamento?
    suspend fun save(item: Departamento): Departamento
    suspend fun update(id: Long, item: Departamento): Departamento?
    suspend fun deleteById(id: Long): Departamento?
}