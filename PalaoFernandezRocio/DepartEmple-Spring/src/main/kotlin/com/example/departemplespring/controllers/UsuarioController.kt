package com.example.departemplespring.controllers

import com.example.departemplespring.config.security.token.JwtTokenUtil
import com.example.departemplespring.dto.UsuarioCreateDto
import com.example.departemplespring.dto.UsuarioDto
import com.example.departemplespring.dto.UsuarioLoginDto
import com.example.departemplespring.dto.UsuarioTokenDto
import com.example.departemplespring.exceptions.UsuarioBadRequestException
import com.example.departemplespring.mappers.toDto
import com.example.departemplespring.mappers.toUsuario
import com.example.departemplespring.models.Usuario
import com.example.departemplespring.services.usuarios.UsuarioService
import com.example.departemplespring.validators.validarCreate
import com.example.departemplespring.validators.validarLogin
import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/usuarios")
class UsuarioController
@Autowired constructor(
    private val service: UsuarioService,
    private val tokenUtil: JwtTokenUtil,
    private val authManager: AuthenticationManager
){

    @PostMapping("/register")
    suspend fun register(@RequestBody dto: UsuarioCreateDto): ResponseEntity<UsuarioTokenDto>{
        try{
            val user = dto.validarCreate().toUsuario()
            val save = service.save(user, user.rol.contains("ADMIN"))
            val token = tokenUtil.generarToken(save)
            return ResponseEntity(UsuarioTokenDto(save.toDto(), token), HttpStatus.CREATED)
        }catch (e: UsuarioBadRequestException){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PostMapping("/login")
    suspend fun login(@RequestBody dto: UsuarioLoginDto): ResponseEntity<UsuarioTokenDto>{
        try{
            val validado = dto.validarLogin()
            val auth: org.springframework.security.core.Authentication? = authManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    validado.username, validado.password
                )
            )
            SecurityContextHolder.getContext().authentication = auth
            val usuario =auth?.principal as Usuario
            val token = tokenUtil.generarToken(usuario)
            return ResponseEntity.ok(UsuarioTokenDto(usuario.toDto(), token))
        }catch (e: UsuarioBadRequestException){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/me")
    suspend fun me(@AuthenticationPrincipal usuario: Usuario): ResponseEntity<UsuarioDto>{
        return ResponseEntity.ok(usuario.toDto())
    }

    @GetMapping("")
    suspend fun findAll():ResponseEntity<List<UsuarioDto>>{
        val lista = service.findAll().map { it.toDto() }
        return ResponseEntity.ok(lista)
    }
}