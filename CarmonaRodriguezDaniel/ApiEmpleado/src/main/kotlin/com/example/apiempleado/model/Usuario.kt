package com.example.apiempleado.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.*


@Table(name = "USUARIOS")
data class Usuario(
    @Id
    val id: Long? = null,
    val uuid: String = UUID.randomUUID().toString(),

    val nombre: String,
    @get:JvmName("userName")
    val username: String,
    val email: String,
    @get:JvmName("userPassword")
    var password: String,
    val avatar: String,

    var rol: String = Rol.USER.name,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column("updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val deleted: Boolean = false,
    @Column("last_password_change_at")
    val lastPasswordChangeAt: LocalDateTime = LocalDateTime.now(),
) : UserDetails {

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