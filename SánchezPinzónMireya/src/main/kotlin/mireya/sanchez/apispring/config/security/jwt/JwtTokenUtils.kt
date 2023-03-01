package mireya.sanchez.apispring.config.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import mireya.sanchez.apispring.models.Usuario
import org.springframework.stereotype.Component
import java.util.*

private val jwtSecreto: Algorithm = Algorithm.HMAC512("EjercicioSpring")

@Component
class JwtTokenUtils {
    fun create(user: Usuario): String {
        return JWT.create()
            .withSubject(user.id.toString())
            .withHeader(mapOf("typ" to TOKEN_TYPE))
            .withClaim("username", user.username)
            .withClaim("rol", user.rol)
            .withExpiresAt(Date(System.currentTimeMillis() + (30 * 60000)))
            .sign(jwtSecreto)
    }

    fun decode(token: String): DecodedJWT? {
        val verifier = JWT.require(jwtSecreto)
            .build()

        return try {
            verifier.verify(token)

        } catch (_: Exception) {
            null

        }
    }

    companion object {
        const val TOKEN_HEADER = "Authorization"
        const val TOKEN_PREFIX = "Bearer "
        const val TOKEN_TYPE = "JWT"
    }
}