package es.dam.springrest.services

import kotlinx.coroutines.flow.Flow

interface IService<T, ID> {
    suspend fun findAll(): Flow<T>
    suspend fun findById(id: ID): T?
    suspend fun save(entity: T): T
    suspend fun update(id: ID, entity: T): T?
    suspend fun delete(id: ID): T?
}