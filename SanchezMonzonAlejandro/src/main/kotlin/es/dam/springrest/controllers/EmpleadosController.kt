package es.dam.springrest.controllers

import es.dam.springrest.config.APIConfig
import es.dam.springrest.dto.EmpleadoResponseDTO
import es.dam.springrest.exceptions.EmpleadoBadRequestException
import es.dam.springrest.mappers.toEmpleado
import es.dam.springrest.models.Empleado
import es.dam.springrest.services.empleados.EmpleadosService
import es.dam.springrest.validations.validarCreate
import jakarta.validation.Valid
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(APIConfig.API_PATH + "/empleados")
class EmpleadosController
@Autowired constructor(
    private val empleadosService: EmpleadosService
) {
    @GetMapping("")
    suspend fun findAll(): ResponseEntity<List<Empleado>> {
        return ResponseEntity.ok(empleadosService.findAll().toList())
    }

    @GetMapping("/{id}")
    suspend fun findById(@PathVariable id: String): ResponseEntity<Empleado> {
        try {
            val entity = empleadosService.findById(id.toLong())
            return ResponseEntity.ok(entity)

        } catch (e: EmpleadoBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }

    }

    @PostMapping("")
    suspend fun save(@Valid @RequestBody entity: EmpleadoResponseDTO): ResponseEntity<Empleado> {
        try {
            return ResponseEntity(empleadosService.save(entity.validarCreate().toEmpleado()), HttpStatus.CREATED)
        } catch (e: EmpleadoBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PutMapping("/{id}")
    suspend fun update(
        @PathVariable id: String,
        @Valid @RequestBody entity: EmpleadoResponseDTO
    ): ResponseEntity<Empleado> {
        try {
            return ResponseEntity.ok(empleadosService.update(id.toLong(), entity.validarCreate().toEmpleado()))
        } catch (e: EmpleadoBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: String): ResponseEntity<Empleado> {
        try {
            return ResponseEntity(HttpStatus.NO_CONTENT)
        } catch (e: EmpleadoBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }
}