package com.example.apiempleado.repository

import com.example.apiempleado.model.Usuario
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuarioRepository : CoroutineCrudRepository<Usuario, Long> {
    fun findByUsername(username: String): Flow<Usuario>
    fun findByUuid(uuid: String): Flow<Usuario>
    fun findByEmail(email: String): Flow<Usuario>
}