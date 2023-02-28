package resa.mario.config.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.stereotype.Component
import resa.mario.models.Usuario
import java.util.*

private val algorithm: Algorithm = Algorithm.HMAC512("123456")

/**
 * Clase encargada de generar y verificar los tokens de los usuarios; JWT
 *
 */
@Component
class JwtTokenUtils {
    fun create(user: Usuario): String {
        return JWT.create()
            .withSubject(user.id.toString())
            .withHeader(mapOf("typ" to TOKEN_TYPE))
            .withClaim("username", user.username)
            .withClaim("role", user.role)
            .withExpiresAt(Date(System.currentTimeMillis() + (20000 * 60))) // 2 minutos
            .sign(algorithm)
    }

    fun decode(token: String): DecodedJWT? {
        val verifier = JWT.require(algorithm)
            .build()

        return try {
            verifier.verify(token)
        } catch (_: Exception) {
            null
        }
    }

    // Nos permite obtener los campos importantes de manera sencilla
    companion object {

        const val TOKEN_HEADER = "Authorization"
        const val TOKEN_PREFIX = "Bearer " // Importante
        const val TOKEN_TYPE = "JWT"
    }
}