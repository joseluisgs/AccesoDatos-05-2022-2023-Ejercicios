package es.dam.springrest.repositories.empleados

import es.dam.springrest.models.Empleado
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmpleadosRepository: CoroutineCrudRepository<Empleado, Long> {
}