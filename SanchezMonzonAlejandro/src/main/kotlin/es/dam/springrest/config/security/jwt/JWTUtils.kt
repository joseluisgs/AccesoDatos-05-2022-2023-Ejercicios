package es.dam.springrest.config.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import es.dam.springrest.models.Usuario
import org.springframework.stereotype.Component
import java.util.*

@Component
class JWTUtils {
    fun create(usuario: Usuario): String {
        return JWT.create()
            .withSubject(usuario.id.toString())
            .withHeader(mapOf("typ" to TOKEN_TYPE))
            .withClaim("username", usuario.username)
            .withClaim("role", usuario.role)
            .withExpiresAt(Date(System.currentTimeMillis() + (50000 * 60)))
            .sign(Algorithm.HMAC512("repaso"))
    }

    fun verify(token: String): DecodedJWT? {
        val verifier = JWT.require(Algorithm.HMAC512("repaso"))
            .build()

        return try {
            verifier.verify(token)
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        const val TOKEN_PREFIX = "Bearer "
        const val TOKEN_TYPE = "JWT"
    }
}