package mendoza.acosta.empresarestspringboot.controller

import jakarta.validation.Valid
import kotlinx.coroutines.flow.toList
import mendoza.acosta.empresarestspringboot.config.APIConfig
import mendoza.acosta.empresarestspringboot.dto.EmpleadoCreateDto
import mendoza.acosta.empresarestspringboot.dto.EmpleadoDto
import mendoza.acosta.empresarestspringboot.exception.EmpleadoBadRequestException
import mendoza.acosta.empresarestspringboot.exception.EmpleadoNotFoundException
import mendoza.acosta.empresarestspringboot.mappers.toDto
import mendoza.acosta.empresarestspringboot.mappers.toModel
import mendoza.acosta.empresarestspringboot.service.empleado.EmpleadoService
import mendoza.acosta.empresarestspringboot.validators.validate
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

private val logger = KotlinLogging.logger { }

@RestController
@RequestMapping(APIConfig.API_PATH + "/empleados")
class EmpleadoController
@Autowired constructor(
    private val empleadoService: EmpleadoService
) {

    @GetMapping("")
    suspend fun findAll(): ResponseEntity<List<EmpleadoDto>> {
        logger.info { "Get All empleados" }
        val res = empleadoService.findAll().toList().map { it.toDto() }
        return ResponseEntity.ok(res)
    }

    @GetMapping("/{id}")
    suspend fun findById(@PathVariable id: UUID): ResponseEntity<EmpleadoDto> {
        logger.info { "Get empleado by UUID $id" }
        try {
            val empleado = empleadoService.findByUuid(id)
            val res = empleado.toDto()
            return ResponseEntity.ok(res)
        } catch (e: EmpleadoNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }

    @PostMapping("")
    suspend fun create(@Valid @RequestBody empleado: EmpleadoCreateDto): ResponseEntity<EmpleadoDto> {
        logger.info { "Post empleado" }
        try {
            val emp = empleado.validate().toModel()
            val res = empleadoService.save(emp).toDto()
            return ResponseEntity.status(HttpStatus.CREATED).body(res)
        } catch (e: EmpleadoNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: EmpleadoBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PutMapping("/{id}")
    suspend fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody empleado: EmpleadoCreateDto
    ): ResponseEntity<EmpleadoDto> {
        logger.info { "Put empleado $id" }
        try {
            val rep = empleado.validate().toModel()
            val res = empleadoService.update(id, rep).toDto()
            return ResponseEntity.status(HttpStatus.OK).body(res)
        } catch (e: EmpleadoNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: EmpleadoBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: UUID): ResponseEntity<EmpleadoDto> {
        logger.info { "Delete empleado $id" }
        try {
            empleadoService.deleteByUuid(id)
            return ResponseEntity.noContent().build()
        } catch (e: EmpleadoNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }

    @GetMapping("find")
    suspend fun findByName(@RequestParam nombre: String): ResponseEntity<List<EmpleadoDto>> {
        logger.info { "Get empleado by nombre $nombre" }
        nombre.let {
            val res = empleadoService.findByNombre(nombre).toList().map { it.toDto() }
            return ResponseEntity.ok(res)
        }
    }
}