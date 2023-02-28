package mireya.sanchez.apispring.controllers

import jakarta.validation.Valid
import kotlinx.coroutines.flow.toList
import mireya.sanchez.apispring.config.APIConfig
import mireya.sanchez.apispring.models.Empleado
import mireya.sanchez.apispring.services.empleado.EmpleadosService
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
    private val service: EmpleadosService
) {
    @GetMapping("/list")
    suspend fun findAll(): ResponseEntity<List<Empleado>> {
        return ResponseEntity.ok(service.findAll().toList())
    }

    @GetMapping("/{id}")
    suspend fun findById(@PathVariable id: String): ResponseEntity<Empleado> {
        try {
            val entity = service.findById(id.toLong())
            return ResponseEntity.ok(entity)

        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }

    }

    @PostMapping("")
    suspend fun create(@Valid @RequestBody entity: Empleado): ResponseEntity<Empleado> {
        return ResponseEntity.ok(service.save(entity))
    }

    @PutMapping("/{id}")
    suspend fun update(@PathVariable id: String, @Valid @RequestBody entity: Empleado
    ): ResponseEntity<Empleado> {
        try {
            return ResponseEntity.ok(service.update(id.toLong(), entity))
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: String): ResponseEntity<Empleado> {
        try {
            return ResponseEntity.ok(service.deleteById(id.toLong()))
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }
}