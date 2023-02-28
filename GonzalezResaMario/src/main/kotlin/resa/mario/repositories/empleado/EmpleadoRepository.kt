package resa.mario.repositories.empleado

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import resa.mario.models.Empleado

@Repository
interface EmpleadoRepository : CoroutineCrudRepository<Empleado, Long> {
}