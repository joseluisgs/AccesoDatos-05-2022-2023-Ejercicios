package daniel.rodriguez.ejerciciospring.controllers

import daniel.rodriguez.ejerciciospring.config.APIConfig
import daniel.rodriguez.ejerciciospring.dto.EmpleadoDTO
import daniel.rodriguez.ejerciciospring.dto.EmpleadoDTOcreacion
import daniel.rodriguez.ejerciciospring.mappers.toDTO
import daniel.rodriguez.ejerciciospring.models.User
import daniel.rodriguez.ejerciciospring.services.EmpleadoService
import daniel.rodriguez.ejerciciospring.validators.validate
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("${APIConfig.API_PATH}/empleados")
class EmpleadoController
@Autowired constructor(
    private val service: EmpleadoService
) {
    @Operation(summary = "Find all empleados.", description = "Endpoint de busqueda de todos los empleados.", tags = ["Empleados"])
    @Parameter(name = "u", description = "Token", required = true)
    @ApiResponse(responseCode = "200", description = "Todos los empleados.")
    @GetMapping("")
    suspend fun findAll(
        @AuthenticationPrincipal u: User
    ): ResponseEntity<List<EmpleadoDTO>> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(service.findAllEmpleados())
    }

    @Operation(summary = "Find by ID.", description = "Endpoint de busqueda de un empleado.", tags = ["Empleados"])
    @Parameter(name = "u", description = "Token", required = true)
    @Parameter(name = "id", description = "UUID", required = true)
    @ApiResponse(responseCode = "200", description = "Empleado con ese id.")
    @ApiResponse(responseCode = "404", description = "Not found.")
    @GetMapping("/{id}")
    suspend fun findByUUID(
        @AuthenticationPrincipal u: User,
        @PathVariable id: UUID
    ): ResponseEntity<EmpleadoDTO> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(service.findEmpleadoByUuid(id).toDTO())
    }

    @Operation(summary = "Create.", description = "Endpoint de creacion de un empleado.", tags = ["Empleados"])
    @Parameter(name = "u", description = "Token", required = true)
    @Parameter(name = "dto", description = "DTO de creacion", required = true)
    @ApiResponse(responseCode = "201", description = "Empleado.")
    @ApiResponse(responseCode = "404", description = "Not found.")
    @ApiResponse(responseCode = "400", description = "Validation error.")
    @PostMapping("")
    suspend fun create(
        @Valid @RequestBody dto: EmpleadoDTOcreacion,
        @AuthenticationPrincipal u: User
    ): ResponseEntity<EmpleadoDTO> = withContext(Dispatchers.IO) {
        dto.validate()
        ResponseEntity.status(HttpStatus.CREATED).body(service.saveEmpleado(dto))
    }

    @Operation(summary = "Update avatar.", description = "Endpoint de modificacion del avatar de un empleado.", tags = ["Empleados"])
    @Parameter(name = "u", description = "Token", required = true)
    @Parameter(name = "file", description = "Avatar", required = true)
    @Parameter(name = "id", description = "UUID", required = true)
    @ApiResponse(responseCode = "200", description = "Empleado.")
    @ApiResponse(responseCode = "404", description = "Not found.")
    @ApiResponse(responseCode = "400", description = "Bad request.")
    @PutMapping("/avatar/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun updateAvatar(
        @AuthenticationPrincipal u: User,
        @RequestPart("file") file: MultipartFile,
        @PathVariable id: UUID
    ): ResponseEntity<EmpleadoDTO> = withContext(Dispatchers.IO) {
        val empleado = service.findEmpleadoByUuid(id)
        val saved = service.updateAvatar(empleado, file)

        ResponseEntity.ok(saved.toDTO())
    }


    @Operation(summary = "Delete empleado.", description = "Endpoint de borrado de un empleado.", tags = ["Empleados"])
    @Parameter(name = "u", description = "Token", required = true)
    @Parameter(name = "id", description = "UUID", required = true)
    @ApiResponse(responseCode = "200", description = "Empleado.")
    @ApiResponse(responseCode = "404", description = "Not found.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    suspend fun delete(
        @AuthenticationPrincipal u: User,
        @PathVariable id: UUID
    ): ResponseEntity<EmpleadoDTO> = withContext(Dispatchers.IO) {
        ResponseEntity.ok(service.deleteEmpleado(id))
    }

    // Este es solo para la carga de datos iniciales de empleados
    suspend fun createInit(dto: EmpleadoDTOcreacion) = withContext(Dispatchers.IO) {
        service.saveEmpleado(dto)
    }
}