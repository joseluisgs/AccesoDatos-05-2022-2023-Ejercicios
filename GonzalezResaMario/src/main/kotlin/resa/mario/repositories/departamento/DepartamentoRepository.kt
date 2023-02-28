package resa.mario.repositories.departamento

import resa.mario.models.Departamento
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DepartamentoRepository : CoroutineCrudRepository<Departamento, Long> {
}