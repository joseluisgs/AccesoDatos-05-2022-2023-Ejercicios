package resa.mario.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

/**
 * Modelo Usuario
 *
 * @property id
 * @property username
 * @property password
 * @property role
 * @property createdAt
 */
@Table("USUARIOS")
data class Usuario(
    @Id
    val id: Long? = null,
    @get:JvmName("userName") // Para que no se llame getUsername
    val username: String,
    @get:JvmName("userPassword") // Para que no se llame getPassword
    val password: String,
    var role: String = Role.USER.name,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
) : UserDetails {

    enum class Role {
        USER, ADMIN
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authority = SimpleGrantedAuthority("ROLE_$role")
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
