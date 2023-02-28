package mendoza.acosta.empresarestspringboot.config.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mendoza.acosta.empresarestspringboot.dto.UsuarioLoginDto
import mendoza.acosta.empresarestspringboot.models.Usuario
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*

class JwtAuthenticationFilter(
    private val jwtToken: JwtToken,
    private val authenticationManager: AuthenticationManager
) : UsernamePasswordAuthenticationFilter() {
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        logger.info { "Autenticando..." }
        val credentials = ObjectMapper().readValue(request.inputStream, UsuarioLoginDto::class.java)
        val auth = UsernamePasswordAuthenticationToken(
            credentials.username,
            credentials.password
        )
        return authenticationManager.authenticate(auth)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        chain: FilterChain?,
        authResult: Authentication
    ) {
        logger.info { "Autenticación correcta" }
        val usuario = authResult.principal as Usuario
        val token: String = jwtToken.generateToken(usuario)
        response.addHeader("Authorization", token)
        response.addHeader("Access-Control-Expose-Headers", JwtToken.TOKEN_HEADER)
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {
        logger.info { "Autenticación incorrecta" }
        val error = BadCredentialsError()
        response.status = error.status
        response.contentType = "application/json"
        response.writer.append(error.toString())
    }

}

private data class BadCredentialsError(
    val timestamp: Long = Date().time,
    val status: Int = 401,
    val message: String = "Usuario o password incorrectos"
) {
    override fun toString(): String {
        return ObjectMapper().writeValueAsString(this)
    }
}