package es.ruymi.departamentoempleadoruben.controller

import es.ruymi.departamentoempleadoruben.config.APIConfig
import es.ruymi.departamentoempleadoruben.config.security.jwt.JwtTokenUtils
import es.ruymi.departamentoempleadoruben.dto.EmpleadoDTO
import es.ruymi.departamentoempleadoruben.exceptions.EmpleadoBadRequestException
import es.ruymi.departamentoempleadoruben.exceptions.EmpleadoNotFoundException
import es.ruymi.departamentoempleadoruben.mapper.toDto
import es.ruymi.departamentoempleadoruben.mapper.toEntity
import es.ruymi.departamentoempleadoruben.services.empleados.EmpleadosService
import es.ruymi.departamentoempleadoruben.validator.validate
import jakarta.validation.Valid
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping(
    APIConfig.API_PATH + "/empleados")
class EmpleadoController
@Autowired constructor(
    private val empleadosService: EmpleadosService
){
    @GetMapping("")
    suspend fun finAll(): ResponseEntity<List<EmpleadoDTO>> {
        val res = empleadosService.findAll()
            .toList()
            .map { it.toDto() }
        return ResponseEntity.ok(res)
    }

    @GetMapping("/{id}")
    suspend fun findById(@PathVariable id: UUID): ResponseEntity<EmpleadoDTO> {
        try {
            val empleado = empleadosService.findByUuid(id)
            val res = empleado?.toDto()
            return ResponseEntity.ok(res)
        } catch (e: EmpleadoNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("")
    suspend fun create(@Valid @RequestBody empleadoDTO: EmpleadoDTO): ResponseEntity<EmpleadoDTO> {
        try {
            val rep = empleadoDTO.validate().toEntity()
            val res = empleadosService.save(rep).toDto()
            return ResponseEntity.status(HttpStatus.CREATED).body(res)
        } catch (e: EmpleadoNotFoundException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{id}")
    suspend fun update(@PathVariable id: UUID, @Valid @RequestBody empleadoDTO: EmpleadoDTO
    ): ResponseEntity<EmpleadoDTO> {
        try {
            val empleado = empleadoDTO.validate().toEntity()
            val res = empleadosService.update(id, empleado)?.toDto()
            return ResponseEntity.status(HttpStatus.OK).body(res)
        } catch (e: EmpleadoNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: EmpleadoBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: UUID): ResponseEntity<EmpleadoDTO> {
        try {
            empleadosService.deleteByUuid(id)
            return ResponseEntity.noContent().build()
        } catch (e: EmpleadoNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }

}