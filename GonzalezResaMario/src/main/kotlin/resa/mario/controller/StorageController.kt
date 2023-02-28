package resa.mario.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import resa.mario.config.APIConfig
import resa.mario.services.storage.StorageServiceImpl
import java.time.LocalDateTime

/**
 * Controlador de prueba para probar la subida de ficheros
 *
 * @property storageService
 */
@RestController
@RequestMapping(APIConfig.API_PATH + "/storage")
class StorageController
@Autowired constructor(
    private val storageService: StorageServiceImpl
){

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