package com.example.departemplespring.repositories.empleados

import com.example.departemplespring.models.Empleado
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EmpleadoRepository: CoroutineCrudRepository<Empleado, Long> {
    fun findEmpleadosByUuidDepartamento(uuid: UUID): Flow<Empleado>
    fun findEmpleadoByEmail(email: String): Flow<Empleado>
}