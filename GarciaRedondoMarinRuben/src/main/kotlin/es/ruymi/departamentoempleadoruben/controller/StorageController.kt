package es.ruymi.departamentoempleadoruben.controller

import es.ruymi.departamentoempleadoruben.config.APIConfig
import es.ruymi.departamentoempleadoruben.exceptions.StorageBadRequestException
import es.ruymi.departamentoempleadoruben.exceptions.StorageException
import es.ruymi.departamentoempleadoruben.services.storage.StorageService
import org.springframework.core.io.Resource
import jakarta.servlet.http.HttpServletRequest
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
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
    @PostMapping(
        value = [""],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
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

}