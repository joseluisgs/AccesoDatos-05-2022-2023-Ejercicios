package daniel.rodriguez.ejerciciospring.services

import daniel.rodriguez.ejerciciospring.dto.UserDTO
import daniel.rodriguez.ejerciciospring.dto.UserDTOcreacion
import daniel.rodriguez.ejerciciospring.exception.UserExceptionNotFound
import daniel.rodriguez.ejerciciospring.mappers.fromDTO
import daniel.rodriguez.ejerciciospring.mappers.toDTO
import daniel.rodriguez.ejerciciospring.models.User
import daniel.rodriguez.ejerciciospring.repositories.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService
@Autowired constructor(
    private val repo: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails = runBlocking {
        repo.findByUsername(username).firstOrNull()
            ?: throw UserExceptionNotFound("User with username $username not found.")
    }

    suspend fun findUserByUuid(uuid: UUID): UserDTO = withContext(Dispatchers.IO) {
        repo.findByUuid(uuid).firstOrNull()?.toDTO()
            ?: throw UserExceptionNotFound("Couldn't find user with uuid $uuid.")
    }

    suspend fun findAllUsers(): List<UserDTO> = withContext(Dispatchers.IO) {
        repo.findAll().toList().map { it.toDTO() }
    }

    suspend fun saveUser(entity: UserDTOcreacion): User = withContext(Dispatchers.IO) {
        val user = entity.fromDTO()
        val new = user.copy(password = passwordEncoder.encode(user.password))
        repo.save(new)
    }

    suspend fun deleteUser(id: UUID): UserDTO = withContext(Dispatchers.IO) {
        val user = repo.findByUuid(id).firstOrNull()
            ?: throw UserExceptionNotFound("User with uuid $id not found.")

        user.id?.let { repo.deleteById(it) }
        user.toDTO()
    }
}