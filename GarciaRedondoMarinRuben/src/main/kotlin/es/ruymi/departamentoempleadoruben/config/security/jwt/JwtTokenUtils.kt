package es.ruymi.departamentoempleadoruben.config.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import es.ruymi.departamentoempleadoruben.exceptions.TokenInvalidException
import es.ruymi.departamentoempleadoruben.models.User
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtTokenUtils {
    @Value("\${jwt.secret:MeGustanLosPepinosDeLeganes}")
    private val jwtSecreto: String? = null

    @Value("\${jwt.token-expiration:3600}")
    private val jwtDuracionTokenEnSegundos = 0

    fun generateToken(user: User): String {
        val tokenExpirationDate = Date(System.currentTimeMillis() + jwtDuracionTokenEnSegundos * 1000)

        return JWT.create()
            .withSubject(user.uuid.toString())
            .withHeader(mapOf("typ" to TOKEN_TYPE))
            .withIssuedAt(Date())
            .withExpiresAt(tokenExpirationDate)
            .withClaim("username", user.username)
            .withClaim("correo", user.correo)
            .withClaim("roles", user.rol.split(",").toSet().toString())
            .sign(Algorithm.HMAC512(jwtSecreto))
    }

    fun getUserIdFromJwt(token: String?): String {
        return validateToken(token!!)!!.subject
    }

    fun validateToken(authToken: String): DecodedJWT? {
        try {
            return JWT.require(Algorithm.HMAC512(jwtSecreto)).build().verify(authToken)
        } catch (e: Exception) {
            throw TokenInvalidException("Token no válido o expirado")
        }
    }

    private fun getClaimsFromJwt(token: String) =
        validateToken(token)?.claims

    fun getUsernameFromJwt(token: String): String {
        val claims = getClaimsFromJwt(token)
        return claims!!["username"]!!.asString()
    }

    fun getRolesFromJwt(token: String): String {
        val claims = getClaimsFromJwt(token)
        return claims!!["roles"]!!.asString()
    }

    fun isTokenValid(token: String): Boolean {
        val claims = getClaimsFromJwt(token)!!
        val expirationDate = claims["exp"]!!.asDate()
        val now = Date(System.currentTimeMillis())
        return now.before(expirationDate)
    }

    companion object {
        const val TOKEN_HEADER = "Authorization"
        const val TOKEN_PREFIX = "Bearer "
        const val TOKEN_TYPE = "JWT"
    }
}