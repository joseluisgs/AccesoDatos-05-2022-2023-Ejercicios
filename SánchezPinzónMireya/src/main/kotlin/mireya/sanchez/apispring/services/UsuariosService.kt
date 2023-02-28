package mireya.sanchez.apispring.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mireya.sanchez.apispring.dto.UsuarioRegisterDTO
import mireya.sanchez.apispring.exceptions.UsuarioNotFoundException
import mireya.sanchez.apispring.mapper.toUsuario
import mireya.sanchez.apispring.models.Usuario
import mireya.sanchez.apispring.repositories.usuario.UsuariosRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UsuariosService
@Autowired constructor(
    private val repository: UsuariosRepository,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails = runBlocking {
        return@runBlocking repository.findByUsername(username)
            ?: throw UsuarioNotFoundException("Usuario no encontrado")
    }

    suspend fun register(entity: UsuarioRegisterDTO): Usuario = withContext(Dispatchers.IO) {
        val user = entity.toUsuario()
        val newUser = user.copy(
            password = passwordEncoder.encode(entity.password)
        )

        repository.save(newUser)
    }

    suspend fun findAll() = withContext(Dispatchers.IO) {
        return@withContext repository.findAll()
    }

    suspend fun findById(id: Long) = withContext(Dispatchers.IO) {
        return@withContext repository.findById(id)
    }
}
