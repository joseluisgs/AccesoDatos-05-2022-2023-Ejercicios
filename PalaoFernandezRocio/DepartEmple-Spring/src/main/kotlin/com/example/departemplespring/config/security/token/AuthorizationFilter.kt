package com.example.departemplespring.config.security.token

import com.example.departemplespring.services.usuarios.UsuarioService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class AuthorizationFilter(
    private val jwtTokenUtil: JwtTokenUtil,
    private val service: UsuarioService,
    authManager: AuthenticationManager
): BasicAuthenticationFilter(authManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val header = request.getHeader(AUTHORIZATION)
        if (header == null || !header.startsWith(JwtTokenUtil.TOKEN_PREFIX)){
            chain.doFilter(request, response)
            return
        }
        getAuthentication(header.substring(7))?.also {
            SecurityContextHolder.getContext().authentication = it
        }
        chain.doFilter(request, response)
    }

    private fun getAuthentication(token :String): UsernamePasswordAuthenticationToken? = runBlocking {
        if (!jwtTokenUtil.tokenValid(token)) return@runBlocking null
        val userId = jwtTokenUtil.getJwtUserId(token)
        val user = service.findById(userId!!.toLong())
        return@runBlocking UsernamePasswordAuthenticationToken(
            user, null, user?.authorities
        )
    }
}