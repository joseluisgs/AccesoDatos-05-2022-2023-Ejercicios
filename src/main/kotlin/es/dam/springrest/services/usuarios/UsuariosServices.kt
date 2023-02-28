package es.dam.springrest.services.usuarios

import es.dam.springrest.dto.UsuarioRegisterDTO
import es.dam.springrest.exceptions.UsuarioException
import es.dam.springrest.exceptions.UsuarioNotFoundException
import es.dam.springrest.mappers.toModel
import es.dam.springrest.models.Usuario
import es.dam.springrest.repositories.usuarios.UsuariosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UsuariosServices
    @Autowired constructor(
        private val usuariosRepository: UsuariosRepository,
        private val passwordEncoder: PasswordEncoder
    ): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails = runBlocking {
        return@runBlocking usuariosRepository.findByUsername(username) ?:
        throw UsuarioNotFoundException("Usuario con nombre $username no encontrado.")
    }

    suspend fun findAll() = withContext(Dispatchers.IO) {
        return@withContext usuariosRepository.findAll()
    }

    suspend fun findById(id: Long): Usuario? = withContext(Dispatchers.IO) {
        return@withContext usuariosRepository.findById(id) ?:
        throw UsuarioNotFoundException("Usuario $id no encontrado.")
    }

    suspend fun registrar(entity: UsuarioRegisterDTO): Usuario = withContext(Dispatchers.IO) {
        val usuario = entity.toModel()
        val registrado = usuario.copy(
            password = passwordEncoder.encode(entity.password)
        )

        usuariosRepository.save(registrado)
        return@withContext registrado
    }
}