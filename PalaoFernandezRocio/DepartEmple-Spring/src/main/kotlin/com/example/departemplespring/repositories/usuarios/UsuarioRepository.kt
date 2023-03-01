package com.example.departemplespring.repositories.usuarios

import com.example.departemplespring.models.Usuario
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuarioRepository: CoroutineCrudRepository<Usuario, Long> {
    fun findUsuarioByUsername(username: String): Flow<Usuario>
    fun findUsuarioByEmail(email: String): Flow<Usuario>
}