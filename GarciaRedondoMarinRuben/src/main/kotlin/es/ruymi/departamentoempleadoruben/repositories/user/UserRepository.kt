package es.ruymi.departamentoempleadoruben.repositories.user

import es.ruymi.departamentoempleadoruben.models.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : CoroutineCrudRepository<User, Long> {
    fun findByUuid(uuid: UUID): Flow<User>
    fun findByUsuario(username: String): Flow<User>
    fun findByCorreo(email: String): Flow<User>
}