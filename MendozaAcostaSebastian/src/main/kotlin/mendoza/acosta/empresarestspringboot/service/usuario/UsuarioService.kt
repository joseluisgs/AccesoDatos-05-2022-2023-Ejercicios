package mendoza.acosta.empresarestspringboot.service.usuario

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mendoza.acosta.empresarestspringboot.exception.UsuarioBadRequestException
import mendoza.acosta.empresarestspringboot.exception.UsuarioException
import mendoza.acosta.empresarestspringboot.exception.UsuarioNotFoundException
import mendoza.acosta.empresarestspringboot.models.Usuario
import mendoza.acosta.empresarestspringboot.repositories.usuario.UsuarioRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

private val logger = KotlinLogging.logger { }

@Service
class UsuarioService
@Autowired constructor(
    private val repository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails = runBlocking {
        return@runBlocking repository.findByUsername(username).firstOrNull()
            ?: throw UsuarioNotFoundException("Usuario no encontrado con username $username")
    }

    suspend fun findAll() = withContext(Dispatchers.IO) {
        return@withContext repository.findAll()
    }

    @Cacheable("usuarios")
    suspend fun loadUsuarioByUuid(uuid: UUID) = withContext(Dispatchers.IO) {
        return@withContext repository.findByUuid(uuid).firstOrNull()
    }

    suspend fun save(usuario: Usuario, isAdmin: Boolean = false): Usuario = withContext(Dispatchers.IO) {
        logger.info { "Guardando usuario ${usuario.username}" }
        if (repository.findByUsername(usuario.username).firstOrNull() != null) {
            logger.info { "El usuario ya existe" }
            throw UsuarioBadRequestException("El username ya existe")
        }
        if (repository.findByEmail(usuario.email).firstOrNull() != null) {
            logger.info { "El email ya existe" }
            throw UsuarioBadRequestException("El email ya existe")
        }
        logger.info { "Guardando..." }
        var nuevoUsuario = usuario.copy(
            uuid = UUID.randomUUID(),
            password = passwordEncoder.encode(usuario.password),
            rol = Usuario.Rol.USER,
            createdAt = LocalDateTime.now()
        )
        if (isAdmin)
            nuevoUsuario = nuevoUsuario.copy(
                rol = Usuario.Rol.ADMIN
            )
        println(nuevoUsuario)
        try {
            return@withContext repository.save(nuevoUsuario)
        } catch (e: UsuarioException) {
            throw UsuarioBadRequestException("Error al crear el usuario: Nombre y/o email ya existen")
        }
    }

    suspend fun update(usuario: Usuario) = withContext(Dispatchers.IO) {
        logger.info { "Actualizando usuario ${usuario.username}" }
        var existe = repository.findByUsername(usuario.username).firstOrNull()
        if (existe != null && existe.id != usuario.id) {
            throw UsuarioBadRequestException("El username ya existe")
        }
        existe = repository.findByEmail(usuario.email).firstOrNull()
        if (existe != null && existe.id != usuario.id) {
            throw UsuarioBadRequestException("El email ya existe")
        }

        logger.info { "Actualizando..." }
        val actualizado = usuario.copy(
            updatedAt = LocalDateTime.now()
        )
        try {
            return@withContext repository.save(actualizado)
        } catch (e: UsuarioException) {
            throw UsuarioBadRequestException("Error al actualizar el usuario: Nombre y/o ya existen")
        }
    }

}