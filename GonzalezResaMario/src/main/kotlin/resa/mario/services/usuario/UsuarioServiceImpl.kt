package resa.mario.services.usuario

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import resa.mario.dto.UsuarioDTORegister
import resa.mario.mappers.toUsuario
import resa.mario.models.Usuario
import resa.mario.repositories.usuario.UsuarioRepository

private val log = KotlinLogging.logger {}

/**
 * Servicio encargado de realizar operaciones sobre Departamentos, haciendo uso del repositorio necesario y del
 * PasswordEncoder
 *
 * @property repository
 * @property passwordEncoder
 */
@Service
class UsuarioServiceImpl
@Autowired constructor(
    private val repository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails = runBlocking {
        return@runBlocking repository.findByUsername(username)
            ?: throw Exception("Usuario no encontrado con username: $username")
    }

    suspend fun register(entity: UsuarioDTORegister): Usuario = withContext(Dispatchers.IO) {
        log.info { "Almacenando usuario: ${entity.username}" }

        val user = entity.toUsuario()
        // Encriptamos la clave del usuario usando la configuracion de PasswordEncoder
        val newUser = user.copy(
            password = passwordEncoder.encode(entity.password)
        )

        repository.save(newUser)
    }

    suspend fun findAll() = withContext(Dispatchers.IO) {
        log.info { "Obteniendo a todos los usuarios" }

        return@withContext repository.findAll()
    }

    suspend fun findById(id: Long) = withContext(Dispatchers.IO) {
        log.info { "Buscando usuario con id: $id" }

        return@withContext repository.findById(id)
    }

}