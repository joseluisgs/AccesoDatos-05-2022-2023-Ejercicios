package com.example.apiempleado.repository.departamento

import com.example.apiempleado.model.Departamento
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DepartamentoRepository : CoroutineCrudRepository<Departamento, Long>