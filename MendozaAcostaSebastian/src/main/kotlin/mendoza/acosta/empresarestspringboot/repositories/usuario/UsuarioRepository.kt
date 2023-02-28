package mendoza.acosta.empresarestspringboot.repositories.usuario

import kotlinx.coroutines.flow.Flow
import mendoza.acosta.empresarestspringboot.models.Usuario
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UsuarioRepository : CoroutineCrudRepository<Usuario, UUID> {
    fun findByUuid(uuid: UUID): Flow<Usuario>
    fun findByUsername(username: String): Flow<Usuario>
    fun findByEmail(email: String): Flow<Usuario>
}