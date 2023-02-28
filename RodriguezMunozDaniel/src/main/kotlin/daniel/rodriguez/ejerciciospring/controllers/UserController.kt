package daniel.rodriguez.ejerciciospring.controllers

import daniel.rodriguez.ejerciciospring.config.APIConfig
import daniel.rodriguez.ejerciciospring.config.security.jwt.JwtTokensUtils
import daniel.rodriguez.ejerciciospring.dto.*
import daniel.rodriguez.ejerciciospring.exception.UserExceptionBadRequest
import daniel.rodriguez.ejerciciospring.mappers.toDTO
import daniel.rodriguez.ejerciciospring.models.User
import daniel.rodriguez.ejerciciospring.services.UserService
import daniel.rodriguez.ejerciciospring.validators.validate
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
import java.util.*

@RestController
@RequestMapping("${APIConfig.API_PATH}/users")
class UserController
@Autowired constructor(
    private val service: UserService,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenUtils: JwtTokensUtils
) {
    @Operation(summary = "Register", description = "Endpoint de registro de usuarios.", tags = ["Users"])
    @Parameter(name = "dto", description = "DTO de creacion", required = true)
    @ApiResponse(responseCode = "201", description = "Usuario y token.")
    @ApiResponse(responseCode = "401", description = "Error: Incorrect user or password.")
    @ApiResponse(responseCode = "400", description = "Validation error.")
    @PostMapping("/register")
    suspend fun register(
        @Valid @RequestBody dto: UserDTOcreacion
    ): ResponseEntity<UserDTOandToken> = withContext(Dispatchers.IO) {
        dto.validate()
        try {
            val saved = service.saveUser(dto)
            ResponseEntity.status(HttpStatus.CREATED).body(UserDTOandToken(saved.toDTO(), jwtTokenUtils.create(saved)))
        } catch (e: Exception) {
            throw UserExceptionBadRequest(e.message)
        }
    }

    @Operation(summary = "Login", description = "Endpoint de login de usuarios.", tags = ["Users"])
    @Parameter(name = "dto", description = "DTO de logado", required = true)
    @ApiResponse(responseCode = "200", description = "Usuario y token.")
    @ApiResponse(responseCode = "401", description = "Error: Incorrect user or password.")
    @ApiResponse(responseCode = "400", description = "Validation error.")
    @PostMapping("/login")
    suspend fun login(
        @Valid @RequestBody dto: UserDTOlogin
    ): ResponseEntity<UserDTOandToken> = withContext(Dispatchers.IO) {
        dto.validate()
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(dto.username, dto.password)
        )
        SecurityContextHolder.getContext().authentication = authentication

        val user = authentication.principal as User

        ResponseEntity.ok(UserDTOandToken(user.toDTO(), jwtTokenUtils.create(user)))
    }

    @Operation(summary = "Me", description = "Endpoint de tu propio usuario.", tags = ["Users"])
    @Parameter(name = "user", description = "Token", required = true)
    @ApiResponse(responseCode = "200", description = "Tu usuario.")
    @GetMapping("/me")
    suspend fun findMe(
        @AuthenticationPrincipal user: User
    ): ResponseEntity<UserDTO> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(user.toDTO())
    }

    @Operation(summary = "Find All", description = "Endpoint de find all usuarios.", tags = ["Users"])
    @Parameter(name = "user", description = "Token", required = true)
    @ApiResponse(responseCode = "200", description = "Todos los usuarios.")
    @GetMapping("")
    suspend fun findAll(
        @AuthenticationPrincipal user: User
    ): ResponseEntity<List<UserDTO>> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(service.findAllUsers())
    }

    @Operation(summary = "Find by ID", description = "Endpoint de busqueda de un usuario.", tags = ["Users"])
    @Parameter(name = "u", description = "Token", required = true)
    @Parameter(name = "id", description = "UUID", required = true)
    @ApiResponse(responseCode = "200", description = "Usuario con ese id.")
    @ApiResponse(responseCode = "404", description = "Not found.")
    @GetMapping("/{id}")
    suspend fun findByUUID(
        @AuthenticationPrincipal u: User,
        @PathVariable id: UUID
    ): ResponseEntity<UserDTO> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(service.findUserByUuid(id))
    }

    @Operation(summary = "Delete by ID", description = "Endpoint de borrado de un usuario.", tags = ["Users"])
    @Parameter(name = "u", description = "Token", required = true)
    @Parameter(name = "id", description = "UUID", required = true)
    @ApiResponse(responseCode = "200", description = "Usuario borrado.")
    @ApiResponse(responseCode = "404", description = "Not found.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    suspend fun delete(
        @AuthenticationPrincipal u: User,
        @PathVariable id: UUID
    ): ResponseEntity<UserDTO> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(service.deleteUser(id))
    }

    // Esto es unicamente para la carga de datos inicial de usuarios
    suspend fun createInit(dto: UserDTOcreacion) = withContext(Dispatchers.IO) {
        service.saveUser(dto)
    }
}