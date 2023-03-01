package es.dam.springrest.controllers

import es.dam.springrest.config.APIConfig
import es.dam.springrest.config.security.jwt.JWTUtils
import es.dam.springrest.dto.UsuarioLoginDTO
import es.dam.springrest.dto.UsuarioRegisterDTO
import es.dam.springrest.dto.UsuarioResponseDTO
import es.dam.springrest.exceptions.UsuarioBadRequestException
import es.dam.springrest.mappers.toDTO
import es.dam.springrest.models.Usuario
import es.dam.springrest.services.usuarios.UsuariosServices
import es.dam.springrest.validations.validarCreate
import jakarta.validation.Valid
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(APIConfig.API_PATH)
class UsuariosController
@Autowired constructor(
    private val usuariosService: UsuariosServices,
    private val authenticationManager: AuthenticationManager,
    private val jwtUtils: JWTUtils
) {
    @PostMapping("/register")
    suspend fun register(@Valid @RequestBody dto: UsuarioRegisterDTO): ResponseEntity<String> {
        try {
            val usuario = usuariosService.registrar(dto.validarCreate())
            return ResponseEntity.ok(jwtUtils.create(usuario))
        } catch (e: Exception) {
            throw UsuarioBadRequestException("Error al registrar el usuario.")
        }
    }

    @PostMapping("/login")
    suspend fun login(@Valid @RequestBody dto: UsuarioLoginDTO): ResponseEntity<String> {
        val authenticacion = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                dto.username,
                dto.password
            )
        )

        SecurityContextHolder.getContext().authentication = authenticacion
        val usuario = authenticacion.principal as Usuario

        return ResponseEntity.ok(jwtUtils.create(usuario))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    suspend fun list(@AuthenticationPrincipal user: Usuario): ResponseEntity<List<UsuarioResponseDTO>> {
        val list = mutableListOf<UsuarioResponseDTO>()

        usuariosService.findAll().toList().forEach {
            list.add(it.toDTO())
        }

        return ResponseEntity.ok(list)
    }

    @GetMapping("/me")
    suspend fun getMySelf(@AuthenticationPrincipal user: Usuario): ResponseEntity<UsuarioResponseDTO> {
        return ResponseEntity.ok(user.toDTO())
    }
}