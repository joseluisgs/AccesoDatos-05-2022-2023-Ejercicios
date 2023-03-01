package com.example.departemplespring.repositories.departamentos

import com.example.departemplespring.models.Departamento
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DepartamentoRepository: CoroutineCrudRepository<Departamento, Long> {
    fun findDepartamentoByUuid(uuid: UUID): Flow<Departamento>
}