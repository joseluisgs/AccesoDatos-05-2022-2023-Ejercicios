package es.dam.springrest.repositories.usuarios

import es.dam.springrest.models.Usuario
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuariosRepository: CoroutineCrudRepository<Usuario, Long> {
    suspend fun findByUsername(username: String): Usuario?
}