package es.dam.springrest.config.security.jwt

import io.netty.handler.codec.http.HttpHeaderNames.AUTHORIZATION
import es.dam.springrest.services.usuarios.UsuariosServices
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.coroutines.runBlocking
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.io.IOException
import org.springframework.security.core.context.SecurityContextHolder

class JWTAuthorizationFilter(
    private val jwtUtils: JWTUtils,
    private val service: UsuariosServices,
    authManager: AuthenticationManager
) : BasicAuthenticationFilter(authManager) {

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain
    ) {
        val header = req.getHeader(AUTHORIZATION.toString())

        if (header == null || !header.startsWith(JWTUtils.TOKEN_PREFIX)) {
            chain.doFilter(req, res)
            return
        }
        getAuthentication(header.substring(7))?.also {
            SecurityContextHolder.getContext().authentication = it
        }
        chain.doFilter(req, res)
    }

    private fun getAuthentication(token: String): UsernamePasswordAuthenticationToken? = runBlocking {
        val tokenDecoded = jwtUtils.verify(token) ?: return@runBlocking null
        val username = tokenDecoded.getClaim("username").toString().replace("\"", "")
        val user = service.loadUserByUsername(username)

        return@runBlocking UsernamePasswordAuthenticationToken(
            user,
            null,
            user.authorities
        )
    }
}