package mendoza.acosta.empresarestspringboot.controller

import jakarta.validation.Valid
import kotlinx.coroutines.flow.toList
import mendoza.acosta.empresarestspringboot.config.APIConfig
import mendoza.acosta.empresarestspringboot.config.security.jwt.JwtToken
import mendoza.acosta.empresarestspringboot.dto.*
import mendoza.acosta.empresarestspringboot.exception.StorageException
import mendoza.acosta.empresarestspringboot.exception.UsuarioBadRequestException
import mendoza.acosta.empresarestspringboot.mappers.toDto
import mendoza.acosta.empresarestspringboot.mappers.toModel
import mendoza.acosta.empresarestspringboot.models.Usuario
import mendoza.acosta.empresarestspringboot.service.storage.StorageService
import mendoza.acosta.empresarestspringboot.service.usuario.UsuarioService
import mendoza.acosta.empresarestspringboot.validators.validate
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException

private val logger = KotlinLogging.logger { }

@RestController
@RequestMapping(APIConfig.API_PATH + "/usuarios")
class UsuarioController
@Autowired constructor(
    private val usuarioService: UsuarioService,
    private val authenticationManager: AuthenticationManager,
    private val jwtToken: JwtToken,
    private val storageService: StorageService
) {
    @PostMapping("/login")
    fun login(@Valid @RequestBody loginDto: UsuarioLoginDto): ResponseEntity<UserWithTokenDto> {
        logger.info { "Login de usuario: ${loginDto.username}" }
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginDto.username,
                loginDto.password
            )
        )

        SecurityContextHolder.getContext().authentication = authentication
        val usuario = authentication.principal as Usuario
        val jwtToken: String = jwtToken.generateToken(usuario)
        logger.info { "Token de usuario: $jwtToken" }
        val userWithToken = UserWithTokenDto(usuario.toDto(), jwtToken)
        return ResponseEntity.ok(userWithToken)
    }

    @PostMapping("/register")
    suspend fun register(@Valid @RequestBody usuarioDto: UsuarioCreateDto): ResponseEntity<UserWithTokenDto> {
        logger.info { "Registro de usuario: ${usuarioDto.username}" }
        try {
            val usuario = usuarioDto.validate().toModel()
            println(usuario.rol.name)
            val usuarioSaved = usuarioService.save(usuario)
            val jwtToken: String = jwtToken.generateToken(usuarioSaved)
            logger.info { "Token de usuario: $jwtToken" }
            return ResponseEntity.ok(UserWithTokenDto(usuarioSaved.toDto(), jwtToken))
        } catch (e: UsuarioBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    fun meInfo(@AuthenticationPrincipal user: Usuario): ResponseEntity<UsuarioDto> {
        logger.info { "Obteniendo usuario: ${user.username}" }
        return ResponseEntity.ok(user.toDto())
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    suspend fun list(@AuthenticationPrincipal user: Usuario): ResponseEntity<List<UsuarioDto>> {
        logger.info { "Obteniendo lista de usuarios" }
        val res = usuarioService.findAll().toList().map { it.toDto() }
        return ResponseEntity.ok(res)
    }

    @PutMapping("/me")
    suspend fun updateMe(
        @AuthenticationPrincipal usuario: Usuario,
        @Valid @RequestBody usuarioDto: UsuarioUpdateDto
    ): ResponseEntity<UsuarioDto> {
        logger.info { "Actualizando usuario: ${usuario.username}" }
        usuarioDto.validate()
        val usuarioUpdated = usuario.copy(
            nombre = usuarioDto.nombre,
            username = usuarioDto.username,
            email = usuarioDto.email,
        )
        try {
            val usuarioUpdated = usuarioService.update(usuarioUpdated)
            return ResponseEntity.ok(usuarioUpdated.toDto())
        } catch (e: UsuarioBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PatchMapping(value = ["/me"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun updateAvatar(
        @AuthenticationPrincipal user: Usuario,
        @RequestPart("file") file: MultipartFile
    ): ResponseEntity<UsuarioDto> {
        logger.info { "Actualizando avatar de usuario: ${user.username}" }
        try {
            var urlImagen = user.avatar
            if (!file.isEmpty) {
                val imagen: String = storageService.store(file, user.uuid.toString())
                urlImagen = storageService.getUrl(imagen)
            }
            val usuarioAvatar = user.copy(
                avatar = urlImagen
            )

            val userUpdated = usuarioService.update(usuarioAvatar)
            return ResponseEntity.ok(userUpdated.toDto())
        } catch (e: UsuarioBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: StorageException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }
}