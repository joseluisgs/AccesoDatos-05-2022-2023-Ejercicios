package resa.mario.controller

import jakarta.validation.Valid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import resa.mario.config.APIConfig
import resa.mario.dto.EmpleadoDTO
import resa.mario.mappers.toDTO
import resa.mario.mappers.toEmpleado
import resa.mario.services.empleado.EmpleadoServiceImpl
import resa.mario.services.storage.StorageServiceImpl

/**
 * Controlador encargado de hacer las operaciones basicas de los empleados
 *
 * @property service
 * @property storageService
 */
@RestController
@RequestMapping(APIConfig.API_PATH + "/empleados")
class EmpleadoController
@Autowired constructor(
    private val service: EmpleadoServiceImpl,
    private val storageService: StorageServiceImpl
) {
    @GetMapping("")
    suspend fun findAll(): ResponseEntity<List<EmpleadoDTO>> {
        val list = mutableListOf<EmpleadoDTO>()
        service.findAll().toList().forEach { list.add(it.toDTO()) }

        return ResponseEntity.ok(list)
    }

    @GetMapping("id/{id}")
    suspend fun findById(@PathVariable id: String): ResponseEntity<EmpleadoDTO> {
        try {
            val entity = service.findById(id.toLong())
            return ResponseEntity.ok(entity?.toDTO())

        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }

    }

    @PostMapping("")
    suspend fun create(@Valid @RequestBody entity: EmpleadoDTO): ResponseEntity<EmpleadoDTO> {
        val empleado = service.save(entity.toEmpleado())
        return ResponseEntity.ok(empleado.toDTO())
    }

    @PutMapping("update/{id}")
    suspend fun update(
        @PathVariable id: String,
        @Valid @RequestBody entity: EmpleadoDTO
    ): ResponseEntity<EmpleadoDTO> {
        try {
            val empleado = service.update(id.toLong(), entity.toEmpleado())
            return ResponseEntity.ok(empleado?.toDTO())
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PutMapping("update/avatar/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun updateAvatar(
        @PathVariable id: String,
        @RequestPart("file") file: MultipartFile
    ): ResponseEntity<EmpleadoDTO> = runBlocking {
        var empleado = service.findById(id.toLong()) ?: throw Exception("Empleado no encontrado")

        empleado.let {
            if (!file.isEmpty) {
                val myScope = CoroutineScope(Dispatchers.IO)
                val fileStored = myScope.async { storageService.saveFileFromUser(file, empleado.name) }.await()

                val newEmpleado = empleado.copy(
                    avatar = fileStored
                )

                empleado = service.update(id.toLong(), newEmpleado)!!
                return@runBlocking ResponseEntity.ok(empleado.toDTO())
            } else {
                throw Exception("No se puede subir un fichero vacío")
            }
        }
    }

    @DeleteMapping("delete/{id}")
    suspend fun delete(@PathVariable id: String): ResponseEntity<EmpleadoDTO> {
        try {
            val empleado = service.deleteById(id.toLong())
            return ResponseEntity.ok(empleado?.toDTO())
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }
}