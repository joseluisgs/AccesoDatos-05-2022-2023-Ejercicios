package mendoza.acosta.empresarestspringboot.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.*

@Table(name = "usuarios")
data class Usuario(
    @Id
    val id: Long? = null,
    val uuid: UUID = UUID.randomUUID(),
    val nombre: String,
    @get:JvmName("userName")
    val username: String,
    @get:JvmName("password")
    val password: String,
    val email: String,
    val avatar: String,
    val rol: Rol,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column("updated_at")
    val updatedAt: LocalDateTime? = null,
    val deleted: Boolean = false
) : UserDetails {
    enum class Rol {
        USER, ADMIN
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return rol.name.split(",").map { SimpleGrantedAuthority("ROLE_${it.trim()}") }.toMutableList()
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
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