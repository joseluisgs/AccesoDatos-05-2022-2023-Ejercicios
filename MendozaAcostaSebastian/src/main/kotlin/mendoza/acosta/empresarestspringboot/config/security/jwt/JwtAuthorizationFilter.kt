package mendoza.acosta.empresarestspringboot.config.security.jwt


import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.coroutines.runBlocking
import mendoza.acosta.empresarestspringboot.service.usuario.UsuarioService
import mendoza.acosta.empresarestspringboot.utils.toUUID
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.io.IOException
import kotlin.jvm.Throws

class JwtAuthorizationFilter(
    private val jwtToken: JwtToken,
    private val service: UsuarioService,
    authManager: AuthenticationManager
) : BasicAuthenticationFilter(authManager) {

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        logger.info { "Filtrando" }
        val header = request.getHeader(AUTHORIZATION)
        if (header == null || !header.startsWith(JwtToken.TOKEN_PREFIX)) {
            chain.doFilter(request, response)
            return
        }
        getAuthentication(header.substring(7))?.also {
            SecurityContextHolder.getContext().authentication = it
        }
        chain.doFilter(request, response)
    }

    private fun getAuthentication(token: String): UsernamePasswordAuthenticationToken? = runBlocking {
        logger.info { "Obteniendo autenticación" }
        if (!jwtToken.isTokenValid(token)) return@runBlocking null
        val usuarioId = jwtToken.getUserIdFromJwt(token)
        val usuario = service.loadUsuarioByUuid(usuarioId.toUUID())
        return@runBlocking UsernamePasswordAuthenticationToken(
            usuario, null, usuario?.authorities
        )
    }

}