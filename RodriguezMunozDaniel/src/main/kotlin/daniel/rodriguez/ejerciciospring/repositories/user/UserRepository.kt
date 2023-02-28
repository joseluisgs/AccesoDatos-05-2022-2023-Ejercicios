package daniel.rodriguez.ejerciciospring.repositories.user

import daniel.rodriguez.ejerciciospring.models.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository  : CoroutineCrudRepository<User, Long> {
    fun findByUuid(uuid: UUID): Flow<User>
    fun findByUsername(username: String): Flow<User>
}