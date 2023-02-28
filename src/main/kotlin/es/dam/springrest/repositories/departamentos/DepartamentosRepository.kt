package es.dam.springrest.repositories.departamentos

import es.dam.springrest.models.Departamento
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DepartamentosRepository: CoroutineCrudRepository<Departamento, Long> {
}