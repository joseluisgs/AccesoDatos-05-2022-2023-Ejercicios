package mendoza.acosta.empresarestspringboot.controller

import jakarta.servlet.http.HttpServletRequest
import kotlinx.coroutines.*
import mendoza.acosta.empresarestspringboot.config.APIConfig
import mendoza.acosta.empresarestspringboot.exception.StorageBadRequestException
import mendoza.acosta.empresarestspringboot.exception.StorageException
import mendoza.acosta.empresarestspringboot.service.storage.StorageService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping(APIConfig.API_PATH + "/storage")
class StorageController
@Autowired constructor(
    private val storageService: StorageService
) {
    @GetMapping(value = ["{filename:.+}"])
    @ResponseBody
    fun serveFile(@PathVariable filename: String?, request: HttpServletRequest): ResponseEntity<Resource> =
        runBlocking {
            logger.info { "GET File: $filename" }
            val myScope = CoroutineScope(Dispatchers.IO)
            val file: Resource = myScope.async { storageService.loadAsResource(filename.toString()) }.await()
            var contentType: String?
            contentType = try {
                request.servletContext.getMimeType(file.file.absolutePath)
            } catch (ex: IOException) {
                throw StorageBadRequestException("No se puede determinar el tipo del fichero", ex)
            }
            if (contentType == null) {
                contentType = "application/octet-stream"
            }
            return@runBlocking ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body<Resource?>(file)
        }

    @PostMapping(value = [""], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadFile(@RequestPart("file") file: MultipartFile): ResponseEntity<Map<String, String>> = runBlocking {
        logger.info { "POST File: ${file.originalFilename}" }
        return@runBlocking try {
            if (!file.isEmpty) {
                val myScope = CoroutineScope(Dispatchers.IO)
                val fileStored = myScope.async { storageService.store(file) }.await()
                val urlStored = storageService.getUrl(fileStored)
                val response =
                    mapOf("url" to urlStored, "name" to fileStored, "created_at" to LocalDateTime.now().toString())
                ResponseEntity.status(HttpStatus.CREATED).body(response)
            } else {
                throw StorageBadRequestException("No se puede subir un fichero vacío")
            }
        } catch (e: StorageException) {
            throw StorageBadRequestException(e.message.toString())
        }
    }

    @DeleteMapping(value = ["{filename:.+}"])
    @ResponseBody
    fun deleteFile(@PathVariable filename: String?, request: HttpServletRequest): ResponseEntity<Resource> =
        runBlocking {
            logger.info { "DELETE File: $filename" }
            try {
                val myScope = CoroutineScope(Dispatchers.IO)
                myScope.launch { storageService.delete(filename.toString()) }.join()
                return@runBlocking ResponseEntity.ok().build()
            } catch (e: StorageException) {
                throw StorageBadRequestException(e.message.toString())
            }
        }
}