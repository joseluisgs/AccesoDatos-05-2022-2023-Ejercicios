package com.example.departemplespring.config.security.token

import com.example.departemplespring.dto.UsuarioLoginDto
import com.example.departemplespring.models.Usuario
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*

class AuthenticationFilter(
    private val jwtTokenUtil: JwtTokenUtil,
    private val authManager: AuthenticationManager
): UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val credentials = ObjectMapper().readValue(request?.inputStream, UsuarioLoginDto::class.java)
        val auth = UsernamePasswordAuthenticationToken(
            credentials.username,
            credentials.password
        )
        return authManager.authenticate(auth)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        val user = authResult?.principal as Usuario
        val token: String = jwtTokenUtil.generarToken(user)
        response?.addHeader("Authorization",token)
        response?.addHeader("Access-Control-Expose-Headers", JwtTokenUtil.TOKEN_HEADER)
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        failed: AuthenticationException?
    ) {
        val error = BadCredentialsError()
        response?.status = error.status
        response?.contentType ="application/json"
        response?.writer?.append(error.toString())
    }


    private data class BadCredentialsError(
        val timestamp: Long = Date().time,
        val status: Int = 401,
        val message: String = "Usuario o password incorrectos",
    ) {
        override fun toString(): String {
            return ObjectMapper().writeValueAsString(this)
        }
    }
}