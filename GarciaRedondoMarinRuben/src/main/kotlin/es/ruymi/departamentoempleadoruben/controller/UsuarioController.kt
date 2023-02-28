package es.ruymi.departamentoempleadoruben.controller

import es.ruymi.departamentoempleadoruben.config.APIConfig
import es.ruymi.departamentoempleadoruben.config.security.jwt.JwtTokenUtils
import es.ruymi.departamentoempleadoruben.dto.*
import es.ruymi.departamentoempleadoruben.exceptions.UserBadRequestException
import es.ruymi.departamentoempleadoruben.mapper.toDto
import es.ruymi.departamentoempleadoruben.mapper.toEntity
import es.ruymi.departamentoempleadoruben.models.User
import es.ruymi.departamentoempleadoruben.services.storage.StorageService
import es.ruymi.departamentoempleadoruben.services.user.UserService
import es.ruymi.departamentoempleadoruben.validator.validate
import jakarta.validation.Valid
import kotlinx.coroutines.flow.toList
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

@RequestMapping(APIConfig.API_PATH + "/users")
class UsuarioController
@Autowired constructor(
    private val usuariosService: UserService,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenUtil: JwtTokenUtils,
    ){
    @PostMapping("/login")
    fun login( @RequestBody logingDto: UserLoginDto): ResponseEntity<UserWithTokenDto> {

        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                logingDto.usuario,
                logingDto.password
            )
        )
        SecurityContextHolder.getContext().authentication = authentication
        val user = authentication.principal as User
        val jwtToken: String = jwtTokenUtil.generateToken(user)
        val userWithToken = UserWithTokenDto(user.toDto(), jwtToken)
        return ResponseEntity.ok(userWithToken)
    }

    @PostMapping("/register")
    suspend fun register(@Valid @RequestBody usuarioDto: UserCreateDto): ResponseEntity<UserWithTokenDto> {
        try {
            val user = usuarioDto.validate().toEntity()
            user.rol.forEach { println(it) }
            val userSaved = usuariosService.save(user)
            val jwtToken: String = jwtTokenUtil.generateToken(userSaved)
            return ResponseEntity.ok(UserWithTokenDto(userSaved.toDto(), jwtToken))
        } catch (e: UserBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/me")
    fun meInfo(@AuthenticationPrincipal user: User): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(user.toDto())
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    suspend fun list(@AuthenticationPrincipal user: User): ResponseEntity<List<UserDTO>> {
        val res = usuariosService.findAll().toList().map { it.toDto() }
        return ResponseEntity.ok(res)
    }


    @PutMapping("/me")
    suspend fun updateMe(
        @AuthenticationPrincipal user: User,
        @Valid @RequestBody usuarioDto: UserUpdateDto
    ): ResponseEntity<UserDTO> {
        usuarioDto.validate()
        val userUpdated = user.copy(
            correo = usuarioDto.correo,
            usuario = usuarioDto.usuario,
        )
        try {
            val userUpdated = usuariosService.update(userUpdated)
            return ResponseEntity.ok(userUpdated.toDto())
        } catch (e: UserBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

}