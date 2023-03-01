package com.example.apiempleado.config.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.apiempleado.exception.TokenInvalidException
import com.example.apiempleado.model.Usuario
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

private val logger = KotlinLogging.logger {}

@Component
class JwtTokenUtils {
    companion object {
        const val TOKEN_HEADER = "Authorization"
        const val TOKEN_PREFIX = "Bearer "
        const val TOKEN_TYPE = "JWT"
    }

    @Value("\${jwt.secret}")
    private val jwtSecret: String? = null

    @Value("\${jwt.token-expiration}")
    private val jwtTokenExpiration: Int? = null

    fun generateJwtToken(user: Usuario): String {
        logger.info { "Generando token para el usuario: ${user.username}" }
        val tokenExpiration = Date(System.currentTimeMillis() + jwtTokenExpiration!! * 1000)
        return JWT.create()
            .withSubject(user.uuid)
            .withHeader(mapOf("typ" to TOKEN_TYPE))
            .withIssuedAt(Date())
            .withExpiresAt(tokenExpiration)
            .withClaim("username", user.username)
            .withClaim("name", user.nombre)
            .withClaim("roles", user.rol.split(",").toSet().toString())
            .sign(Algorithm.HMAC512(jwtSecret))
    }

    fun validateToken(authToken: String): DecodedJWT? {
        logger.info { "Validando el token: ${authToken}" }
        try {
            return JWT.require(Algorithm.HMAC512(jwtSecret)).build().verify(authToken)
        } catch (e: Exception) {
            throw TokenInvalidException("Token no válido o expirado")
        }
    }

    private fun getClaimsFromJwt(token: String) = validateToken(token)?.claims

    fun getUuidFromJwt(token: String): String {
        logger.info { "Obteniendo el UUID del usuario: $token" }
        return validateToken(token)!!.subject
    }

    fun getUserName(token: String): String {
        val claims = getClaimsFromJwt(token)
        return claims!!["username"]!!.asString()
    }

    fun getRolesName(token: String): String {
        val claims = getClaimsFromJwt(token)
        return claims!!["roles"]!!.asString()
    }

    fun isTokenValid(token: String): Boolean {
        logger.info { "Comprobando si el token es válido: ${token}" }
        val claims = getClaimsFromJwt(token)
        val expirateDate = claims!!["exp"]!!.asDate()
        val now = Date(System.currentTimeMillis())
        return now.before(expirateDate)
    }
}