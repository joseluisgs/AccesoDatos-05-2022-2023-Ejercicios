package com.example.apiempleado.config.security.jwt

import com.example.apiempleado.dto.UsuarioLoginDto
import com.example.apiempleado.model.Usuario
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*

class JwtAuthenticationFilter(
    private val jwtTokenUtils: JwtTokenUtils,
    private val authManager: AuthenticationManager,
) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        logger.info { "Intentando autenticar" }
        val json: String = request.inputStream.bufferedReader().use { it.readLines() }.joinToString(",")
        val credentials = Json.decodeFromString<UsuarioLoginDto>(json)
//            val credentials = ObjectMapper().readValue(request.inputStream, UsuarioLoginDto::class.java)
        val auth = UsernamePasswordAuthenticationToken(
            credentials.username,
            credentials.password
        )
        return authManager.authenticate(auth)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication,
    ) {
        logger.info { "Autenticación correcta" }

        val user = authResult.principal as Usuario
        val token = jwtTokenUtils.generateJwtToken(user)
        response.addHeader(HttpHeaders.AUTHORIZATION, token)
        response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, JwtTokenUtils.TOKEN_HEADER)
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException,
    ) {
        println("Autenticación Incorrecta")
        val error = BadCredentialError()
        response.status = error.status
        response.contentType = "application/json"
        response.writer.append(error.toString())
    }
}

private data class BadCredentialError(
    val time: Long = Date().time,
    val status: Int = 401,
    val message: String = "Usuario o password incorrectos.",
) {
    override fun toString(): String {
        return ObjectMapper().writeValueAsString(this)
    }
}