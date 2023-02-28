package mireya.sanchez.apispring.repositories.empleado

import mireya.sanchez.apispring.models.Empleado
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmpleadosRepository : CoroutineCrudRepository<Empleado, Long> {
}