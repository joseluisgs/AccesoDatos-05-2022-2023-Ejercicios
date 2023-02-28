package resa.mario.config.security.jwt

import io.netty.handler.codec.http.HttpHeaderNames.AUTHORIZATION
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import resa.mario.services.usuario.UsuarioServiceImpl
import java.io.IOException

private val log = KotlinLogging.logger {}

/**
 * Clase encargada del filtrado de autorizaciones
 *
 * @property jwtTokenUtils
 * @property service
 * @constructor
 * El generador y verificador de tokens, el servicio de usuarios y el AuthenticationManager
 *
 * @param authManager
 */
class JwtAuthorizationFilter(
    private val jwtTokenUtils: JwtTokenUtils,
    private val service: UsuarioServiceImpl,
    authManager: AuthenticationManager
) : BasicAuthenticationFilter(authManager) {

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain
    ) {
        log.info { "Filtrando" }
        val header = req.getHeader(AUTHORIZATION.toString())

        if (header == null || !header.startsWith(JwtTokenUtils.TOKEN_PREFIX)) {
            chain.doFilter(req, res)
            return
        }
        getAuthentication(header.substring(7))?.also {
            SecurityContextHolder.getContext().authentication = it
            //println(it)
        }
        chain.doFilter(req, res)
    }

    private fun getAuthentication(token: String): UsernamePasswordAuthenticationToken? = runBlocking {
        log.info { "Obteniendo autenticación" }

        val tokenDecoded = jwtTokenUtils.decode(token) ?: return@runBlocking null

        val username = tokenDecoded.getClaim("username").toString().replace("\"", "")

        val user = service.loadUserByUsername(username)

        //System.err.println(user)

        return@runBlocking UsernamePasswordAuthenticationToken(
            user,
            null,
            user.authorities
        )
    }
}