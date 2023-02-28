package es.ruymi.departamentoempleadoruben.services.user

import es.ruymi.departamentoempleadoruben.exceptions.UserBadRequestException
import es.ruymi.departamentoempleadoruben.exceptions.UserNotFoundException
import es.ruymi.departamentoempleadoruben.models.User
import es.ruymi.departamentoempleadoruben.repositories.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService
@Autowired constructor(
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {
    override fun loadUserByUsername(usuario: String): UserDetails = runBlocking {
        return@runBlocking repository.findByUsuario(usuario).firstOrNull()
            ?: throw UserNotFoundException("Usuario no encontrado con usuario: $usuario")
    }

    suspend fun findAll() = withContext(Dispatchers.IO) {
        return@withContext repository.findAll()
    }

    @Cacheable("user")
    suspend fun loadUserById(userId: Long) = withContext(Dispatchers.IO) {
        return@withContext repository.findById(userId)
    }

    @Cacheable("user")
    suspend fun loadUserByUuid(uuid: UUID) = withContext(Dispatchers.IO) {
        return@withContext repository.findByUuid(uuid).firstOrNull()
    }

    suspend fun save(user: User, isAdmin: Boolean = false): User = withContext(Dispatchers.IO) {
        if (repository.findByUsuario(user.usuario).firstOrNull() != null) {
            throw UserBadRequestException("El usuario ya existe")
        }
        if (repository.findByCorreo(user.correo).firstOrNull() != null) {
            throw UserBadRequestException("El correo ya existe")
        }

        var newUser = user.copy(
            uuid = UUID.randomUUID(),
            password = passwordEncoder.encode(user.password),
            rol = User.Rol.USER.name,
        )
        if (isAdmin)
            newUser = newUser.copy(
                rol = User.Rol.ADMIN.name
            )
        println(newUser)
        try {
            return@withContext repository.save(newUser)
        } catch (e: Exception) {
            throw UserBadRequestException("Error al crear el usuario: Nombre de usuario o correo ya existentes")
        }
    }

    suspend fun update(user: User) = withContext(Dispatchers.IO) {
        var userDB = repository.findByUsuario(user.usuario).firstOrNull()
        if (userDB != null && userDB.id != user.id) {
            throw UserBadRequestException("El usuario ya existe")
        }
        userDB = repository.findByCorreo(user.correo).firstOrNull()
        if (userDB != null && userDB.id != user.id) {
            throw UserBadRequestException("El correo ya existe")
        }
        try {
            return@withContext repository.save(user)
        } catch (e: Exception) {
            throw UserBadRequestException("Error al actualizar el usuario: Nombre de usuario o correo ya existen")
        }
    }
}