package mendoza.acosta.empresarestspringboot.config.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import mendoza.acosta.empresarestspringboot.exception.TokenInvalidException
import mendoza.acosta.empresarestspringboot.models.Usuario
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

private val logger = KotlinLogging.logger { }

@Component
class JwtToken {
    @Value("\${jwt.secret:ClaveSecretaParaEmpresaRestSpringBoot")
    private val jwtSecreto: String? = null

    @Value("\${jwt.token-expiration:3600}")
    private val jwtExpirationToken = 0

    fun generateToken(usuario: Usuario): String {
        logger.info { "Generando token para el usuario ${usuario.nombre}" }
        val tokenExpiration = Date(System.currentTimeMillis() + jwtExpirationToken * 1000)
        return JWT.create()
            .withSubject(usuario.uuid.toString())
            .withHeader(mapOf("typ" to TOKEN_TYPE))
            .withIssuedAt(Date())
            .withExpiresAt(tokenExpiration)
            .withClaim("username", usuario.username)
            .withClaim("nombre", usuario.nombre)
            .withClaim("rol", usuario.rol.toString())
            .sign(Algorithm.HMAC512(jwtSecreto))
    }

    fun getUserIdFromJwt(token: String?): String {
        logger.info { "Obteniendo el ID del usuario: $token" }
        return validateToken(token!!)!!.subject
    }

    fun validateToken(authToken: String): DecodedJWT? {
        logger.info { "Validando el token: ${authToken}" }

        try {
            return JWT.require(Algorithm.HMAC512(jwtSecreto)).build().verify(authToken)
        } catch (e: Exception) {
            throw TokenInvalidException("Token no válido o expirado")
        }
    }

    private fun getClaimsFromJwt(token: String) =
        validateToken(token)?.claims

    fun isTokenValid(token: String): Boolean {
        logger.info { "Validando token $token" }
        val claims = getClaimsFromJwt(token)!!
        val expirationDate = claims["exp"]!!.asDate()
        val now = Date(System.currentTimeMillis())
        return now.before(expirationDate)
    }

    companion object {
        const val TOKEN_HEADER = "Authorization"
        const val TOKEN_PREFIX = "Bearer"
        const val TOKEN_TYPE = "JWT"
    }
}