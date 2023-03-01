package es.dam.springrest.controllers

import es.dam.springrest.config.APIConfig
import es.dam.springrest.dto.DepartamentoResponseDTO
import es.dam.springrest.exceptions.DepartamentoBadRequestException
import es.dam.springrest.mappers.toDepartamento
import es.dam.springrest.models.Departamento
import es.dam.springrest.services.departamentos.DepartamentosService
import es.dam.springrest.validations.validateCreate
import jakarta.validation.Valid
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(APIConfig.API_PATH + "/departamentos")
class DepartamentosController
@Autowired constructor(
    private val departamentosService: DepartamentosService
) {
    @GetMapping("")
    suspend fun findAll(): ResponseEntity<List<Departamento>> {
        return ResponseEntity.ok(departamentosService.findAll().toList())
    }

    @GetMapping("/{id}")
    suspend fun findById(@PathVariable id: String): ResponseEntity<Departamento> {
        try {
            val entity = departamentosService.findById(id.toLong())
            return ResponseEntity.ok(entity)

        } catch (e: DepartamentoBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }

    }

    @PostMapping("")
    suspend fun save(@Valid @RequestBody entity: DepartamentoResponseDTO): ResponseEntity<Departamento> {
        try {
          return ResponseEntity(departamentosService.save(entity.validateCreate().toDepartamento()), HttpStatus.CREATED)
        } catch (e: DepartamentoBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PutMapping("/{id}")
    suspend fun update(
        @PathVariable id: String,
        @Valid @RequestBody entity: DepartamentoResponseDTO
    ): ResponseEntity<Departamento> {
        try {
            return ResponseEntity.ok(departamentosService.update(id.toLong(), entity.validateCreate().toDepartamento()))
        } catch (e: DepartamentoBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: String): ResponseEntity<Departamento> {
        try {
            return ResponseEntity(HttpStatus.NO_CONTENT)
        } catch (e: DepartamentoBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }
}
