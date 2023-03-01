package daniel.rodriguez.ejerciciospring.repositories

import kotlinx.coroutines.flow.Flow
import java.util.*

interface ICRUDRepository<T, ID> {
    suspend fun findAll(): Flow<T>
    suspend fun findById(id: ID): T?
    suspend fun findByUUID(id: UUID): T?
    suspend fun save(entity: T): T
    suspend fun delete(id: ID): T?
}