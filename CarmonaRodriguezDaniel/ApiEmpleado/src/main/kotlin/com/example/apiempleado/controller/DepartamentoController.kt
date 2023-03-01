package com.example.apiempleado.controller

import com.example.apiempleado.dto.*
import com.example.apiempleado.exception.DepartamentoBadRequestException
import com.example.apiempleado.exception.DepartamentoNotFoundException
import com.example.apiempleado.mapper.toDepartamento
import com.example.apiempleado.mapper.toDepartamentoDto
import com.example.apiempleado.mapper.toEmpleadoDto
import com.example.apiempleado.model.Usuario
import com.example.apiempleado.service.DepartamentoService
import com.example.apiempleado.validators.validate
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/departamentos")
class DepartamentoController
@Autowired constructor(
    private val service: DepartamentoService,
) {
    @GetMapping
    suspend fun findAllEmpleados(): ResponseEntity<List<DepartamentoDto>> {
        return ResponseEntity.ok(service.findAll().toList().map { it.toDepartamentoDto() })
    }

    @GetMapping("/{id}")
    suspend fun findById(@PathVariable("id") id: Long): ResponseEntity<DepartamentoDto> {
        try {
            val exist = service.findById(id)
            return ResponseEntity.ok(exist.toDepartamentoDto())
        } catch (e: DepartamentoNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }

    @GetMapping("/{id}/empleados")
    suspend fun findByEmpleado(@PathVariable id: Long): ResponseEntity<List<EmpleadoDto>> {
        try {
            val departamento = service.findById(id)
            val empleados = service.findByEmpleado(departamento.id!!)
            return ResponseEntity.ok(empleados.toList().map { it.toEmpleadoDto(departamento) })
        } catch (e: DepartamentoNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    suspend fun save(
        @AuthenticationPrincipal user: Usuario,
        @RequestBody departamento: DepartamentoCreateDto,
    ): ResponseEntity<DepartamentoDto> {
        try {
            departamento.validate()
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.save(departamento.toDepartamento()).toDepartamentoDto())
        } catch (e: DepartamentoNotFoundException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    suspend fun update(
        @AuthenticationPrincipal user: Usuario,
        @PathVariable id: Long,
        @RequestBody departamento: DepartamentoCreateDto,
    ): ResponseEntity<DepartamentoDto> {
        try {
            departamento.validate()
            val res = service.update(id, departamento.toDepartamento())
            return ResponseEntity.ok().body(res.toDepartamentoDto())
        } catch (e: DepartamentoBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: DepartamentoNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }
}