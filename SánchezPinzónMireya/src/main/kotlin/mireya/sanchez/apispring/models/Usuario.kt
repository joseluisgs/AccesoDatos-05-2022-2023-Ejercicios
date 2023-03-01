package mireya.sanchez.apispring.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

@Table(name = "Usuarios")
data class Usuario(
    @Id
    val id: Long? = null,
    @get:JvmName("userName")
    val username: String,
    @get:JvmName("userPassword")
    val password: String,
    var rol: String = Rol.USER.name,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
) : UserDetails {

    enum class Rol {
        USER, ADMIN
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authority = SimpleGrantedAuthority("ROLE_$rol")
        return mutableListOf<GrantedAuthority>(authority)
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