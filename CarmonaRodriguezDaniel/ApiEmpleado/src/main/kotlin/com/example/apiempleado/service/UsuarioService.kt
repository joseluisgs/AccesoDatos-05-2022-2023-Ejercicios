package com.example.apiempleado.service

import com.example.apiempleado.exception.UsuarioBadRequestException
import com.example.apiempleado.exception.UsuarioNotFoundException
import com.example.apiempleado.model.Usuario
import com.example.apiempleado.repository.UsuarioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UsuarioService
@Autowired constructor(
    private val repository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails = runBlocking {
        return@runBlocking repository.findByUsername(username).firstOrNull()
            ?: throw UsuarioNotFoundException("No existe el usuario: $username")
    }

    suspend fun loadUserByUuid(uuid: String) = withContext(Dispatchers.IO) {
        return@withContext repository.findByUuid(uuid).firstOrNull()
    }

    suspend fun findAll() = withContext(Dispatchers.IO) {
        return@withContext repository.findAll()
    }

    suspend fun save(user: Usuario, isAdmin: Boolean): Usuario = withContext(Dispatchers.IO) {
        val exist = repository.findByUsername(user.username).firstOrNull()
        if (exist != null) {
            throw UsuarioBadRequestException("Ya existe el username.")
        }
        user.password = passwordEncoder.encode(user.password)
        if (isAdmin) user.rol = Usuario.Rol.ADMIN.name
        return@withContext repository.save(user)
    }

}