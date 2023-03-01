package com.example.apiempleado.controller

import com.example.apiempleado.exception.StorageBadRequestException
import com.example.apiempleado.model.Usuario
import com.example.apiempleado.service.storage.StorageService
import jakarta.servlet.http.HttpServletRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.io.IOException
import java.time.LocalDateTime

@RestController
@RequestMapping("/storage")
class StorageController
@Autowired constructor(
    private val service: StorageService,
) {
    @GetMapping("/{filename:.+}")
    fun getFile(@PathVariable filename: String?, request: HttpServletRequest): ResponseEntity<Resource> = runBlocking {
        println("Obteniendo file: $filename")

        val myScope = CoroutineScope(Dispatchers.IO)
        val file: Resource = myScope.async { service.loadAsResource(filename.toString()) }.await()
        var contentType = try {
            request.servletContext.getMimeType(file.file.absolutePath)
        } catch (e: IOException) {
            throw StorageBadRequestException("No se puede determinar el tipo del fichero : ${e.message}")
        }
        if (contentType == null) {
            contentType = "application/octet-stream"
        }

        return@runBlocking ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .body<Resource?>(file)
    }


    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun saveFile(@RequestPart file: MultipartFile): ResponseEntity<Map<String, String>> = runBlocking {
        println("Almacenando fichero: ${file.originalFilename}")

        return@runBlocking try {
            if (!file.isEmpty) {
                val myScope = CoroutineScope(Dispatchers.IO)
                val fileStored = myScope.async { service.store(file) }.await()
                val url = service.getUrl(fileStored)
                val res = mapOf("url" to url, "name" to fileStored, "created_at" to LocalDateTime.now().toString())
                ResponseEntity.status(HttpStatus.CREATED).body(res)
            } else {
                throw StorageBadRequestException("No se puede subir un fichero vacío")
            }
        } catch (e: StorageBadRequestException) {
            throw StorageBadRequestException(e.message.toString())
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{filename:.+}")
    fun deleteFile(@AuthenticationPrincipal usuario: Usuario, @PathVariable filename: String?): ResponseEntity<Void> {
        return filename?.let {
            service.delete(filename)
            ResponseEntity.noContent().build()
        } ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "El file es incorrecto")
    }
}