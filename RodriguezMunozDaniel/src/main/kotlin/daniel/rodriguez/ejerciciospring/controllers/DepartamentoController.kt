package daniel.rodriguez.ejerciciospring.controllers

import daniel.rodriguez.ejerciciospring.config.APIConfig
import daniel.rodriguez.ejerciciospring.dto.DepartamentoDTO
import daniel.rodriguez.ejerciciospring.dto.DepartamentoDTOcreacion
import daniel.rodriguez.ejerciciospring.models.User
import daniel.rodriguez.ejerciciospring.services.DepartamentoService
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
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("${APIConfig.API_PATH}/departamentos")
class DepartamentoController
@Autowired constructor(
    private val service: DepartamentoService
) {
    @Operation(summary = "Find all departamentos.", description = "Endpoint de busqueda de todos los departamentos.", tags = ["Departamentos"])
    @Parameter(name = "u", description = "Token", required = true)
    @ApiResponse(responseCode = "200", description = "Todos los departamentos.")
    @GetMapping("")
    suspend fun findAll(
        @AuthenticationPrincipal u: User
    ): ResponseEntity<List<DepartamentoDTO>> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(service.findAllDepartamentos())
    }

    @Operation(summary = "Find by ID.", description = "Endpoint de busqueda de un departamento.", tags = ["Departamentos"])
    @Parameter(name = "u", description = "Token", required = true)
    @Parameter(name = "id", description = "UUID", required = true)
    @ApiResponse(responseCode = "200", description = "Departamento con ese id.")
    @ApiResponse(responseCode = "404", description = "Not found.")
    @GetMapping("/{id}")
    suspend fun findByUUID(
        @AuthenticationPrincipal u: User,
        @PathVariable id: UUID
    ): ResponseEntity<DepartamentoDTO> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(service.findDepartamentoByUuid(id))
    }

    @Operation(summary = "Create.", description = "Endpoint de creacion de un departamento.", tags = ["Departamentos"])
    @Parameter(name = "u", description = "Token", required = true)
    @Parameter(name = "dto", description = "DTO de creacion", required = true)
    @ApiResponse(responseCode = "201", description = "Departamento.")
    @ApiResponse(responseCode = "400", description = "Validation error.")
    @PostMapping("")
    suspend fun create(
        @Valid @RequestBody dto: DepartamentoDTOcreacion,
        @AuthenticationPrincipal u: User
    ): ResponseEntity<DepartamentoDTO> = withContext(Dispatchers.IO) {
        dto.validate()
        ResponseEntity.status(HttpStatus.CREATED).body(service.saveDepartamento(dto))
    }

    @Operation(summary = "Delete departamento.", description = "Endpoint de borrado de un departamento.", tags = ["Departamentos"])
    @Parameter(name = "u", description = "Token", required = true)
    @Parameter(name = "id", description = "UUID", required = true)
    @ApiResponse(responseCode = "200", description = "Departamento.")
    @ApiResponse(responseCode = "400", description = "Bad request.")
    @ApiResponse(responseCode = "404", description = "Not found.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    suspend fun delete(
        @AuthenticationPrincipal u: User,
        @PathVariable id: UUID
    ): ResponseEntity<DepartamentoDTO> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(service.deleteDepartamento(id))
    }

    // Este es solo para la carga de datos iniciales de departamento
    suspend fun createInit(dto: DepartamentoDTOcreacion) = withContext(Dispatchers.IO) {
        service.saveDepartamento(dto)
    }
}