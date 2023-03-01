package com.example.departemplespring.controllers

import com.example.departemplespring.dto.EmpleadoCreateDto
import com.example.departemplespring.exceptions.DepartamentoNotFoundException
import com.example.departemplespring.exceptions.EmpleadoBadRequestException
import com.example.departemplespring.exceptions.EmpleadoNotFoundException
import com.example.departemplespring.mappers.toEmpleado
import com.example.departemplespring.models.Empleado
import com.example.departemplespring.services.departamentos.DepartamentoService
import com.example.departemplespring.services.empleados.EmpleadoService
import com.example.departemplespring.validators.validarCreate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
@RequestMapping("/empleados")
class EmpleadoController
@Autowired constructor(
    private val service: EmpleadoService,
    private val departamento: DepartamentoService
) {

    @GetMapping("")
    suspend fun findAll():ResponseEntity<List<Empleado>>{
        val lista = service.findAll()
        return ResponseEntity.ok(lista)
    }


    @GetMapping("/{id}")
    suspend fun findById(@PathVariable id: Long):ResponseEntity<Empleado>{
        try {
            val find = service.findById(id)
            return ResponseEntity.ok(find)
        }catch (e : EmpleadoNotFoundException){
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }


    @PostMapping("")
    suspend fun create(@RequestBody dto: EmpleadoCreateDto): ResponseEntity<Empleado>{
        try{
            val post = dto.validarCreate().toEmpleado()
            val findDpt = departamento.findByUuid(post.uuidDepartamento)
            findDpt?.let {
                val save = service.save(post)
                return ResponseEntity.ok(save)
            }?: run{
                throw ResponseStatusException(HttpStatus.NOT_FOUND, "Departamento con uuid ${post.uuidDepartamento} no encontrado")
            }
        }catch (e: EmpleadoBadRequestException){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }


    @PutMapping("/{id}")
    suspend fun update(
        @RequestBody dto: EmpleadoCreateDto,
        @PathVariable id: Long
    ): ResponseEntity<Empleado>{
        try {
            val empleado = dto.validarCreate().toEmpleado()
            val find = service.findById(id)
            find?.let {
                val findDpt = departamento.findByUuid(it.uuidDepartamento)
                findDpt?.let {
                    empleado.id = find.id
                    val update = service.update(id, empleado)
                    return ResponseEntity.ok(update)
                }?: run{
                    throw ResponseStatusException(HttpStatus.NOT_FOUND, "Departamento con uuid ${find.uuidDepartamento} no encontrado")
                }
            } ?: run {
                throw ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado un empleado con el id: $id")
            }
        }catch (e: EmpleadoBadRequestException){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }


    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id:Long): ResponseEntity<Empleado>{
        val find = service.findById(id)
        find?.let{
            service.deleteById(it.id!!)
            return ResponseEntity(HttpStatus.NO_CONTENT)
        }?:run{
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el empleado con el id: $id")
        }
    }
}