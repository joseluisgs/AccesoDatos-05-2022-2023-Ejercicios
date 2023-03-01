package com.example.apiempleado.controller

import com.example.apiempleado.dto.*
import com.example.apiempleado.exception.DepartamentoNotFoundException
import com.example.apiempleado.exception.EmpleadoBadRequestException
import com.example.apiempleado.exception.EmpleadoNotFoundException
import com.example.apiempleado.mapper.toDepartamentoDto
import com.example.apiempleado.mapper.toEmpleado
import com.example.apiempleado.mapper.toEmpleadoDto
import com.example.apiempleado.model.Usuario
import com.example.apiempleado.service.EmpleadoService
import com.example.apiempleado.validators.validate
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@Controller
@RequestMapping("/empleados")
class EmpleadoController
@Autowired constructor(
    private val service: EmpleadoService,
) {
    @Operation(
        summary = "GetAll Empleados",
        description = "Obtiene una lista de todos los empleados",
        tags = ["Empleado"]
    )
    @ApiResponse(responseCode = "200", description = "Lista de Empleados")
    @GetMapping
    suspend fun findAll(): ResponseEntity<List<EmpleadoDto>> {
        return ResponseEntity.ok(service.findAll().map { it.toEmpleadoDto(service.findDepartamento(it.departamento)) }
            .toList())
    }

    @Operation(summary = "Get by ID", description = "Obtiene un empleado por su ID", tags = ["Empleado"])
    @Parameter(name = "id", description = "ID del empleado", required = true, example = "1")
    @ApiResponse(responseCode = "200", description = "Empleado encontrado")
    @ApiResponse(responseCode = "404", description = "Empleado no encontrado si id = -1")
    @ApiResponse(responseCode = "500", description = "Error interno si id = error")
    @ApiResponse(responseCode = "400", description = "Petición incorrecta si id = otro")
    @GetMapping("/{id}")
    suspend fun findById(@PathVariable("id") id: Long): ResponseEntity<EmpleadoDto> {
        try {
            val exist = service.findById(id)
            return ResponseEntity.ok(exist.toEmpleadoDto(service.findDepartamento(exist.departamento)))
        } catch (e: EmpleadoNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: DepartamentoNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }

    @Operation(
        summary = "Get by departamento ID",
        description = "Obtiene el departamento del empleado id",
        tags = ["Empleado"]
    )
    @Parameter(name = "id", description = "ID del empleado", required = true, example = "1")
    @ApiResponse(responseCode = "200", description = "Empleado encontrado")
    @ApiResponse(responseCode = "404", description = "Empleado no encontrado si id = -1")
    @ApiResponse(responseCode = "500", description = "Error interno si id = error")
    @ApiResponse(responseCode = "400", description = "Petición incorrecta si id = otro")
    @GetMapping("/{id}/departamento")
    suspend fun findDepartamento(@PathVariable id: Long): ResponseEntity<DepartamentoDto> {
        try {
            val empleado = service.findById(id)
            val departamento = service.findDepartamento(empleado.departamento).toDepartamentoDto()
            return ResponseEntity.ok(departamento)
        } catch (e: EmpleadoNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: DepartamentoNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }

    @Operation(summary = "Create Empleado", description = "Crea un empleado", tags = ["Empleado"])
    @Parameter(name = "empleado", description = "Los datos del empleado", required = true)
    @ApiResponse(responseCode = "201", description = "Empleado creado")
    @ApiResponse(responseCode = "404", description = "Departamento no encontrado si id = -1")
    @ApiResponse(responseCode = "500", description = "Error interno si id = error")
    @ApiResponse(responseCode = "400", description = "Petición incorrecta, si los datos no son validos.")
    @ApiResponse(responseCode = "403", description = "No tienes permisos si no eres admin.")
    @ApiResponse(responseCode = "401", description = "No autorizado si no te has logueado.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    suspend fun save(
        @AuthenticationPrincipal user: Usuario,
        @RequestBody empleado: EmpleadoCreateDto,
    ): ResponseEntity<EmpleadoDto> {
        try {
            empleado.validate()
            return ResponseEntity.status(HttpStatus.CREATED).body(
                service.save(empleado.toEmpleado()).toEmpleadoDto(service.findDepartamento(empleado.departamentoId))
            )
        } catch (e: EmpleadoBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: DepartamentoNotFoundException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @Operation(summary = "Update Empleado", description = "Actualiza los datos de un empleado", tags = ["Empleado"])
    @Parameter(name = "id", description = "ID del empleado", required = true, example = "1")
    @Parameter(name = "empleado", description = "Los datos del empleado actualizados", required = true)
    @ApiResponse(responseCode = "200", description = "Empleado Actualizado")
    @ApiResponse(responseCode = "404", description = "Empleado no encontrado si id = -1")
    @ApiResponse(responseCode = "500", description = "Error interno si id = error")
    @ApiResponse(responseCode = "400", description = "Petición incorrecta, si los datos no son validos.")
    @ApiResponse(responseCode = "403", description = "No tienes permisos si no eres admin.")
    @ApiResponse(responseCode = "401", description = "No autorizado si no te has logueado.")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    suspend fun update(
        @AuthenticationPrincipal user: Usuario,
        @PathVariable id: Long,
        @RequestBody empleado: EmpleadoUpdateDto,
    ): ResponseEntity<EmpleadoDto> {
        try {
            empleado.validate()
            val res = service.update(id, empleado)
            val dto = res.toEmpleadoDto(service.findDepartamento(res.departamento))
            return ResponseEntity.ok().body(dto)
        } catch (e: EmpleadoBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: EmpleadoNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }

    @Operation(summary = "Delete Empleado", description = "Elimina el empleado", tags = ["Empleado"])
    @Parameter(name = "id", description = "ID del empleado", required = true, example = "1")
    @ApiResponse(responseCode = "204", description = "Empleado eliminado")
    @ApiResponse(responseCode = "404", description = "Empleado no encontrado si id = -1")
    @ApiResponse(responseCode = "500", description = "Error interno si id = error")
    @ApiResponse(responseCode = "400", description = "Petición incorrecta, si los datos no son validos.")
    @ApiResponse(responseCode = "403", description = "No tienes permisos si no eres admin.")
    @ApiResponse(responseCode = "401", description = "No autorizado si no te has logueado.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    suspend fun delete(@AuthenticationPrincipal user: Usuario, @PathVariable("id") id: Long): ResponseEntity<Void> {
        try {
            val res = service.findById(id)
            service.delete(res)
            return ResponseEntity.noContent().build()
        } catch (e: EmpleadoNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }


}