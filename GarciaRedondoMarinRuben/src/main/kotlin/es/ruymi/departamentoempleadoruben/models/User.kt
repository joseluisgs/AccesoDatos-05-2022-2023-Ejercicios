package es.ruymi.departamentoempleadoruben.models

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Table(name = "USUARIO")
data class User(
    @Id
    val id: Long? = null,
    val uuid: UUID = UUID.randomUUID(),
    @Email(regexp = ".*@.*\\..*", message = "El email debe ser un email valido")
    val correo: String,
    @NotEmpty(message = "El usuario no puede estar vacío")
    @get:JvmName("usuario")
    val usuario: String,
    @NotEmpty(message = "La contraseña no puede estar vacía")
    @get:JvmName("userPassword")
    val password: String,
    val rol: String = Rol.USER.name
): UserDetails {
    enum class Rol {
        USER, ADMIN
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return rol.split(",").map { SimpleGrantedAuthority("ROLE_${it.trim()}") }.toMutableList()
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return usuario
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}