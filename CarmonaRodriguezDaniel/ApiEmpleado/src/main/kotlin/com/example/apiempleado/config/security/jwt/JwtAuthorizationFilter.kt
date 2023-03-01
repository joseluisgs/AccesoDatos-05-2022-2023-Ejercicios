package com.example.apiempleado.config.security.jwt

import com.example.apiempleado.service.UsuarioService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.io.IOException

private val log = KotlinLogging.logger {}

class JwtAuthorizationFilter(
    private val jwtTokenUtils: JwtTokenUtils,
    private val service: UsuarioService,
    authManager: AuthenticationManager,
) : BasicAuthenticationFilter(authManager) {

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
    ) {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (header == null || !header.startsWith(JwtTokenUtils.TOKEN_PREFIX)) {
            chain.doFilter(request, response)
            return
        }
        getAuthentication(header.substring(7))?.also {
            SecurityContextHolder.getContext().authentication = it
        }
        chain.doFilter(request, response)
    }

    private fun getAuthentication(token: String): UsernamePasswordAuthenticationToken? = runBlocking {
        log.info { "Obteniendo autenticación" }

        if (!jwtTokenUtils.isTokenValid(token)) return@runBlocking null
        val uuid = jwtTokenUtils.getUuidFromJwt(token)
        val user = service.loadUserByUuid(uuid)
        return@runBlocking UsernamePasswordAuthenticationToken(
            user,
            null,
            user?.authorities
        )
    }
}