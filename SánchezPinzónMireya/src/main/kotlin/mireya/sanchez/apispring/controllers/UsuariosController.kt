package mireya.sanchez.apispring.controllers

import jakarta.validation.Valid
import kotlinx.coroutines.flow.toList
import mireya.sanchez.apispring.config.APIConfig
import mireya.sanchez.apispring.config.security.jwt.JwtTokenUtils
import mireya.sanchez.apispring.dto.UsuarioDTO
import mireya.sanchez.apispring.dto.UsuarioLoginDTO
import mireya.sanchez.apispring.dto.UsuarioRegisterDTO
import mireya.sanchez.apispring.mapper.toDTO
import mireya.sanchez.apispring.models.Usuario
import mireya.sanchez.apispring.services.UsuariosService
import mireya.sanchez.apispring.validations.validate
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

@RestController
@RequestMapping(APIConfig.API_PATH + "/usuarios")
class UsuariosController
@Autowired constructor(
    private val service: UsuariosService,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenUtils: JwtTokenUtils
) {

    @PostMapping("/register")
    suspend fun register(@Valid @RequestBody usuarioDto: UsuarioRegisterDTO): ResponseEntity<String> {
        try {
            val userSaved = service.register(usuarioDto.validate())

            return ResponseEntity.ok(jwtTokenUtils.create(userSaved))
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @GetMapping("/login")
    suspend fun login(@Valid @RequestBody usuarioDto: UsuarioLoginDTO): ResponseEntity<String> {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                usuarioDto.username,
                usuarioDto.password
            )
        )
        SecurityContextHolder.getContext().authentication = authentication

        val user = authentication.principal as Usuario

        return ResponseEntity.ok(jwtTokenUtils.create(user))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    suspend fun getAll(@AuthenticationPrincipal user: Usuario): ResponseEntity<List<UsuarioDTO>> {
        val list = mutableListOf<UsuarioDTO>()

        service.findAll().toList().forEach { list.add(it.toDTO()) }

        return ResponseEntity.ok(list)
    }

    @GetMapping("/me")
    suspend fun getMySelf(@AuthenticationPrincipal user: Usuario): ResponseEntity<UsuarioDTO> {
        return ResponseEntity.ok(user.toDTO())
    }

    suspend fun initialiceUsers(userDTOregister: UsuarioRegisterDTO) {
        service.register(userDTOregister)
    }
}