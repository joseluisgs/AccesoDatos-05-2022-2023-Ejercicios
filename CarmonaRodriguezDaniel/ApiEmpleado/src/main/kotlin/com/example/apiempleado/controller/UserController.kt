package com.example.apiempleado.controller

import com.example.apiempleado.config.security.jwt.JwtTokenUtils
import com.example.apiempleado.dto.UsuarioCreateDto
import com.example.apiempleado.dto.UsuarioDto
import com.example.apiempleado.dto.UsuarioDtoWithToken
import com.example.apiempleado.dto.UsuarioLoginDto
import com.example.apiempleado.exception.UsuarioBadRequestException
import com.example.apiempleado.mapper.toUsuario
import com.example.apiempleado.mapper.toUsuarioDto
import com.example.apiempleado.mapper.toUsuarioDtoWithToken
import com.example.apiempleado.model.Usuario
import com.example.apiempleado.service.UsuarioService
import com.example.apiempleado.validators.validate
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/users")
class UserController
@Autowired constructor(
    private val service: UsuarioService,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenUtils: JwtTokenUtils,
) {
    @PostMapping("/register")
    suspend fun register(@RequestBody usuario: UsuarioCreateDto): ResponseEntity<UsuarioDtoWithToken> {
        try {
            usuario.validate()
            val isAdmin = usuario.rol.map { it.uppercase() }.contains("ADMIN")
            val user = usuario.toUsuario()
            val userSave = if (isAdmin) service.save(user, true) else service.save(user, false)
            val token = jwtTokenUtils.generateJwtToken(userSave)
            return ResponseEntity.ok(userSave.toUsuarioDtoWithToken(token))
        } catch (e: UsuarioBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody login: UsuarioLoginDto): ResponseEntity<UsuarioDtoWithToken> {
        logger.info { "Login de usuario: ${login.username}" }

        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                login.username,
                login.password
            )
        )

        SecurityContextHolder.getContext().authentication = authentication
        val user = authentication.principal as Usuario

        val token = jwtTokenUtils.generateJwtToken(user)
        return ResponseEntity.status(HttpStatus.CREATED).body(user.toUsuarioDtoWithToken(token))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    suspend fun findAll(@AuthenticationPrincipal user: Usuario): ResponseEntity<List<UsuarioDto>> {
        return ResponseEntity.ok().body(service.findAll().map { it.toUsuarioDto() }.toList())
    }
    
}