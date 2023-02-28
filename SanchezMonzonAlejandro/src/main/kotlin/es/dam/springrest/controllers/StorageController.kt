package es.dam.springrest.controllers

import es.dam.springrest.config.APIConfig
import es.dam.springrest.exceptions.StorageBadRequestException
import es.dam.springrest.services.storage.StorageService
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
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.time.LocalDateTime

@RestController
@RequestMapping(APIConfig.API_PATH + "/storage")
class StorageController
@Autowired constructor(
    private val storageService: StorageService
) {
    @PostMapping(value = [""], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadFile(
        @RequestPart("file") file: MultipartFile
    ): ResponseEntity<Map<String, Any>> = runBlocking {
        if (!file.isEmpty) {
            val myScope = CoroutineScope(Dispatchers.IO)
            val fileStored = myScope.async { storageService.saveFile(file) }.await()
            val response =
                mapOf("name" to fileStored, "created_at" to LocalDateTime.now().toString())
            return@runBlocking ResponseEntity.status(HttpStatus.CREATED).body(response)
        } else {
            throw Exception("No se puede subir un fichero vacío")
        }
    }

    @GetMapping(value = ["{filename:.+}"])
    @ResponseBody
    fun loadFile(
        @PathVariable filename: String?,
        request: HttpServletRequest
    ): ResponseEntity<Resource> = runBlocking {
        val file: Resource = storageService.getFile(filename.toString())
        var contentType: String? = null
        contentType = try {
            request.servletContext.getMimeType(file.file.absolutePath)
        } catch (e: IOException) {
            throw StorageBadRequestException("Error al obtener el fichero.", e)
        }
        if (contentType == null) {
            contentType = "application/octet-stream"
        }
        return@runBlocking ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .body<Resource?>(file)
    }
}