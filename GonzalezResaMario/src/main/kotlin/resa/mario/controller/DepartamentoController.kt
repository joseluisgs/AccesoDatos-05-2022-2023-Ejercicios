package resa.mario.controller

import jakarta.validation.Valid
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import resa.mario.config.APIConfig
import resa.mario.dto.DepartamentoDTO
import resa.mario.mappers.toDTO
import resa.mario.mappers.toDepartamento
import resa.mario.models.Usuario
import resa.mario.services.departamento.DepartamentoServiceImpl

// IMPORTANTE, PARA SPRING SECURITY -> LOS METODOS DE LOS CONTROLADORES EN PUBLICO

/**
 * Controlador encargado de hacer las operaciones basicas de los departamentos
 *
 * @property service
 */
@RestController
@RequestMapping(APIConfig.API_PATH + "/departamentos")
class DepartamentoController
@Autowired constructor(
    private val service: DepartamentoServiceImpl,
) {
    @GetMapping("")
    suspend fun findAll(): ResponseEntity<List<DepartamentoDTO>> {
        val list = mutableListOf<DepartamentoDTO>()
        service.findAll().toList().forEach { list.add(it.toDTO()) }

        return ResponseEntity.ok(list)
    }

    @GetMapping("id/{id}")
    suspend fun findById(@PathVariable id: String): ResponseEntity<DepartamentoDTO> {
        try {
            val departamento = service.findById(id.toLong())
            return ResponseEntity.ok(departamento?.toDTO())

        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }

    }

    @PostMapping("")
    suspend fun create(@Valid @RequestBody entity: DepartamentoDTO): ResponseEntity<DepartamentoDTO> {
        val departamento = service.save(entity.toDepartamento())
        return ResponseEntity.ok(departamento.toDTO())
    }

    @PutMapping("update/{id}")
    suspend fun update(
        @PathVariable id: String,
        @Valid @RequestBody entity: DepartamentoDTO
    ): ResponseEntity<DepartamentoDTO> {
        try {
            val departamento = service.update(id.toLong(), entity.toDepartamento())
            return ResponseEntity.ok(departamento?.toDTO())
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete/{id}")
    suspend fun delete(@PathVariable id: String, @AuthenticationPrincipal user: Usuario): ResponseEntity<DepartamentoDTO> {
        try {
            val departamento = service.deleteById(id.toLong())
            return ResponseEntity.ok(departamento?.toDTO())
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }
}