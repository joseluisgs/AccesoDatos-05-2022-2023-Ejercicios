package com.example.departemplespring.controllers

import com.example.departemplespring.dto.DepartamentoCreateDto
import com.example.departemplespring.exceptions.DepartamentoBadRequestException
import com.example.departemplespring.exceptions.DepartamentoNotFoundException
import com.example.departemplespring.mappers.toDepartamento
import com.example.departemplespring.models.Departamento
import com.example.departemplespring.models.Usuario
import com.example.departemplespring.services.departamentos.DepartamentoService
import com.example.departemplespring.services.empleados.EmpleadoService
import com.example.departemplespring.validators.validateCreate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/departamentos")
class DepartamentoController
@Autowired constructor(
    private val service: DepartamentoService,
    private val empleados: EmpleadoService
){

    @GetMapping("")
    suspend fun findAll(): ResponseEntity<List<Departamento>>{
        val lista = service.findAll()
        return ResponseEntity.ok(lista)
    }


    @GetMapping("/{id}")
    suspend fun findById(@PathVariable id:Long): ResponseEntity<Departamento>{
        try{
            val find = service.findById(id)
            return ResponseEntity.ok(find)
        }catch (e: DepartamentoNotFoundException){
            throw ResponseStatusException(HttpStatus.NOT_FOUND,e.message)
        }
    }


    @PostMapping("")
    suspend fun save(@RequestBody dto: DepartamentoCreateDto): ResponseEntity<Departamento>{
        try {
            val departamento = dto.validateCreate().toDepartamento()
            service.save(departamento)
            return ResponseEntity(departamento, HttpStatus.CREATED)
        }catch (e: DepartamentoBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }


    @PutMapping("/{id}")
    suspend fun update(
        @PathVariable id: Long,
        @RequestBody dto: DepartamentoCreateDto)
    : ResponseEntity<Departamento>{
        try{
            val departamento = dto.validateCreate().toDepartamento()
            val updated = service.update(id, departamento)
            updated?.let {
                return ResponseEntity.ok(updated)
            }?: run{
                throw ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el departamento con id: $id")
            }
        }catch (e: DepartamentoBadRequestException){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    suspend fun delete(
        @AuthenticationPrincipal user: Usuario,
        @PathVariable id: Long)
    : ResponseEntity<Departamento>{
        val find = service.findById(id)
        find?.let {
            val empleados = empleados.findEmpleadosByDepartamentoUuid(it.uuid)
            if (empleados.isEmpty()){
                return ResponseEntity(HttpStatus.NO_CONTENT)
            }else{
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede eliminar un departamento que tiene empleados asociados")
            }
        }?: run{
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el departamento con el id: $id")
        }
    }
}