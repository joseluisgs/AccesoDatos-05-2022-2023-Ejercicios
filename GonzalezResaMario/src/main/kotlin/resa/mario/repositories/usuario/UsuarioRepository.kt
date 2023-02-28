package resa.mario.repositories.usuario

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import resa.mario.models.Usuario

@Repository
interface UsuarioRepository : CoroutineCrudRepository<Usuario, Long> {

    // Metodo usado para Spring Security
    suspend fun findByUsername(username: String): Usuario?
}