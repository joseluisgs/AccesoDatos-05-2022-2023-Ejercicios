package com.example.departemplespring.config.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.departemplespring.exceptions.TokenInvalidException
import com.example.departemplespring.models.Usuario
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenUtil {
    @Value("\${jwt.secret}")
    private val secret: String? = null
    @Value("\${jwt.token-expiration}")
    private val expiration = 0

    fun generarToken(user: Usuario):String{
        val tokenExpirationDate = Date(System.currentTimeMillis() + expiration * 1000)
        return JWT.create()
            .withSubject(user.id.toString())
            .withHeader(mapOf("typ" to TOKEN_TYPE))
            .withIssuedAt(Date())
            .withExpiresAt(tokenExpirationDate)
            .withClaim("username", user.username)
            .withClaim("rol", user.rol.split(",").toSet().toString())
            .sign(Algorithm.HMAC512(secret))
    }

    fun validacionToken(token: String): DecodedJWT?{
        try{
            return JWT.require(Algorithm.HMAC512(secret)).build().verify(token)
        }catch (e: Exception){
            throw TokenInvalidException("Token inválido o expirado")
        }
    }

    fun getJwtClaims(token:String) = validacionToken(token)?.claims

    fun getJwtUserId(token:String) = validacionToken(token)?.subject

    fun tokenValid(token:String): Boolean{
        val claims = getJwtClaims(token)!!
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