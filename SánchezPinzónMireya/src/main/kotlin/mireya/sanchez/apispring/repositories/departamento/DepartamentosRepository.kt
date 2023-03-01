package mireya.sanchez.apispring.repositories.departamento

import mireya.sanchez.apispring.models.Departamento
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DepartamentosRepository : CoroutineCrudRepository<Departamento, Long> {
}