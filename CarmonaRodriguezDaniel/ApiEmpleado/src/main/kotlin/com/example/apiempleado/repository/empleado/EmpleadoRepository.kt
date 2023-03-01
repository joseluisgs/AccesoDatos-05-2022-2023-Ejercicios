package com.example.apiempleado.repository.empleado

import com.example.apiempleado.model.Empleado
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmpleadoRepository : CoroutineCrudRepository<Empleado, Long>