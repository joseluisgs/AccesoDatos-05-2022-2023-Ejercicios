package com.example.departemplespring.services.empleados

import com.example.departemplespring.models.Empleado
import java.util.*

interface EmpleadoService {
    suspend fun findAll(): List<Empleado>
    suspend fun findById(id: Long): Empleado?
    suspend fun save(item: Empleado): Empleado
    suspend fun update(id: Long, item: Empleado): Empleado?
    suspend fun deleteById(id: Long): Empleado?
    suspend fun findEmpleadosByDepartamentoUuid(departamentoUuid: UUID): List<Empleado>
}