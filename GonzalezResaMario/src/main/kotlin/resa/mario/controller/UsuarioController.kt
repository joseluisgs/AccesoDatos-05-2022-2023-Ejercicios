package resa.mario.controller

import jakarta.validation.Valid
import kotlinx.coroutines.flow.toList
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import resa.mario.config.APIConfig
import resa.mario.config.security.jwt.JwtTokenUtils
import resa.mario.dto.UsuarioDTOLogin
import resa.mario.dto.UsuarioDTORegister
import resa.mario.dto.UsuarioDTOResponse
import resa.mario.mappers.toDTO
import resa.mario.models.Usuario
import resa.mario.services.usuario.UsuarioServiceImpl

private val log = KotlinLogging.logger {}

/**
 * Controlador encargado de hacer las operaciones basicas de los usuarios
 *
 * @property service
 * @property authenticationManager
 * @property jwtTokenUtils
 */
@RestController
@RequestMapping(APIConfig.API_PATH + "/usuarios")
class UsuarioController
@Autowired constructor(
    private val service: UsuarioServiceImpl,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenUtils: JwtTokenUtils
) {

    @PostMapping("/register")
    suspend fun register(@Valid @RequestBody usuarioDto: UsuarioDTORegister): ResponseEntity<String> {
        log.info { "Registro de usuario: ${usuarioDto.username}" }

        try {
            val userSaved = service.register(usuarioDto)

            return ResponseEntity.ok(jwtTokenUtils.create(userSaved))
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    @GetMapping("/login")
    suspend fun login(@Valid @RequestBody usuarioDto: UsuarioDTOLogin): ResponseEntity<String> {
        log.info { "Login de usuario: ${usuarioDto.username}" }

        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                usuarioDto.username,
                usuarioDto.password
            )
        )
        // Autenticamos al usuario, si lo es nos lo devuelve
        SecurityContextHolder.getContext().authentication = authentication

        // Devolvemos al usuario autenticado
        val user = authentication.principal as Usuario

        return ResponseEntity.ok(jwtTokenUtils.create(user))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    suspend fun getAll(@AuthenticationPrincipal user: Usuario): ResponseEntity<List<UsuarioDTOResponse>> {
        log.info { "Obteniendo listado de usuarios" }

        val list = mutableListOf<UsuarioDTOResponse>()

        service.findAll().toList().forEach { list.add(it.toDTO()) }

        return ResponseEntity.ok(list)
    }

    @GetMapping("/me")
    suspend fun getMySelf(@AuthenticationPrincipal user: Usuario): ResponseEntity<UsuarioDTOResponse> {
        log.info { "Obteniendo datos propios del usuario" }

        return ResponseEntity.ok(user.toDTO())
    }

    // Este metodo solo es para introduccion de usuarios base para la app
    suspend fun createByAdminInitializer(userDTOregister: UsuarioDTORegister) {
        log.info { "Creando usuario por parte de un administrador || Carga de datos inicial" }

        service.register(userDTOregister)
    }
}