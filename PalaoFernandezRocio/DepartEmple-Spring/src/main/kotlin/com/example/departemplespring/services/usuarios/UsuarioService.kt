package com.example.departemplespring.services.usuarios

import com.example.departemplespring.exceptions.EmpleadoNotFoundException
import com.example.departemplespring.exceptions.UsuarioBadRequestException
import com.example.departemplespring.exceptions.UsuarioNotFoundException
import com.example.departemplespring.models.Departamento
import com.example.departemplespring.models.Rol
import com.example.departemplespring.models.Usuario
import com.example.departemplespring.repositories.usuarios.UsuarioRepository
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
import java.time.LocalDateTime
import java.util.*

@Service
class UsuarioService
@Autowired constructor(
    private val repository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails = runBlocking{
        return@runBlocking repository.findUsuarioByUsername(username).firstOrNull()
            ?: throw UsuarioNotFoundException("No se ha encontrado un usuario con el username: $username")
    }


    suspend fun findAll(): List<Usuario> = withContext(Dispatchers.IO) {
        return@withContext repository.findAll().toList()
    }


    suspend fun findById(id: Long): Usuario? = withContext(Dispatchers.IO) {
        return@withContext repository.findById(id)
            ?: throw UsuarioNotFoundException("No se ha encontrado un usuario con el id :$id")
    }


    suspend fun save(item: Usuario, isAdmin: Boolean = false): Usuario = withContext(Dispatchers.IO) {
        if (repository.findUsuarioByUsername(item.username)
                .firstOrNull() != null
        ) {
            throw UsuarioBadRequestException("El username ya existe")
        }
        if (repository.findUsuarioByEmail(item.email)
                .firstOrNull() != null
        ) {
            throw UsuarioBadRequestException("El email ya existe")
        }

        var newUser = item.copy(
            password = passwordEncoder.encode(item.password),
            rol = Rol.USER.name,
        )
        if (isAdmin)
            newUser = newUser.copy(
                rol = Rol.ADMIN.name
            )
        try {
            return@withContext repository.save(newUser)
        } catch (e: Exception) {
            throw UsuarioBadRequestException("Error al crear el usuario: Nombre de usuario o email ya existen")
        }
    }


    suspend fun update(id: Long, item: Usuario): Usuario? = withContext(Dispatchers.IO) {
        var userDB = repository.findUsuarioByUsername(item.username).firstOrNull()
        if (userDB != null && userDB.id != item.id) {
            throw UsuarioBadRequestException("Ya exite este username")
        }

        userDB = repository.findUsuarioByEmail(item.email).firstOrNull()
        if (userDB != null && userDB.id != item.id) {
            throw UsuarioBadRequestException("Ya existe este email")
        }

        var find = repository.findById(id)
        find?.let {
            val updtatedUser = item.copy(
                id = it.id
            )
            try {
                return@withContext repository.save(updtatedUser)
            } catch (e: Exception) {
                throw UsuarioBadRequestException("Error al actualizar el usuario: Nombre de usuario o email ya existen")
            }
        } ?: run{
            throw UsuarioNotFoundException("No se ha encontrado un usuario con el id: $id")
        }
    }


    suspend fun deleteById(id: Long): Usuario? = withContext(Dispatchers.IO) {
        val find = repository.findById(id)
        find?.let {
            repository.delete(it)
            return@withContext it
        }?: throw UsuarioNotFoundException("No existe un usuario con el id: $id")
    }
}