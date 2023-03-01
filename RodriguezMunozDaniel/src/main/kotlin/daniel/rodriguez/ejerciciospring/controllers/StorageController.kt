package daniel.rodriguez.ejerciciospring.controllers

import daniel.rodriguez.ejerciciospring.config.APIConfig
import daniel.rodriguez.ejerciciospring.exception.StorageException
import daniel.rodriguez.ejerciciospring.exception.StorageExceptionBadRequest
import daniel.rodriguez.ejerciciospring.services.storage.IStorageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.servlet.http.HttpServletRequest
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.time.LocalDateTime

@RestController
@RequestMapping("${APIConfig.API_PATH}/storage")
class StorageController
@Autowired constructor(
    private val storageService: IStorageService
) {
    @Operation(
        summary = "Find file.",
        description = "Endpoint de busqueda de un avatar por nombre.",
        tags = ["Storage"]
    )
    @Parameter(name = "filename", description = "Archivo a buscar", required = false)
    @Parameter(name = "request", description = "HttpServletRequest", required = false)
    @ApiResponse(responseCode = "200", description = "Archivo buscado.")
    @ApiResponse(responseCode = "400", description = "Bad request.")
    @GetMapping(value = ["{filename:.+}"])
    @ResponseBody
    fun serveFile(@PathVariable filename: String?, request: HttpServletRequest?): ResponseEntity<Resource> =
        runBlocking {
            val file: Resource =
                CoroutineScope(Dispatchers.IO).async { storageService.loadAsResource(filename.toString()) }.await()
            var contentType: String?
            if (request == null) ResponseEntity.ok().body(file)
            else {
                contentType = try {
                    request.servletContext.getMimeType(file.file.absolutePath)
                } catch (ex: IOException) {
                    throw StorageExceptionBadRequest("Unable to guess file type.", ex)
                }
                if (contentType == null) contentType = "application/octet-stream"
                ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(file)
            }
        }

    @Operation(summary = "Upload.", description = "Endpoint de subida de un avatar.", tags = ["Storage"])
    @Parameter(name = "file", description = "Archivo a subir", required = true)
    @ApiResponse(responseCode = "201", description = "Archivo subido.")
    @ApiResponse(responseCode = "400", description = "Bad request.")
    @PostMapping(value = [""], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadFile(
        @RequestPart("file") file: MultipartFile
    ): ResponseEntity<Map<String, String>> = runBlocking {
        try {
            if (!file.isEmpty) {
                val myScope = CoroutineScope(Dispatchers.IO)
                val fileStored = myScope.async { storageService.store(file) }.await()
                val url = storageService.getUrl(fileStored)
                val response = mapOf("url" to url, "name" to fileStored, "created_at" to LocalDateTime.now().toString())
                ResponseEntity.status(HttpStatus.CREATED).body(response)
            } else throw StorageExceptionBadRequest("You cannot upload an empty file.")
        } catch (e: StorageException) {
            throw StorageExceptionBadRequest(e.message.toString())
        }
    }

    @Operation(summary = "Delete.", description = "Endpoint de borrado de un avatar.", tags = ["Storage"])
    @Parameter(name = "filename", description = "Nombre del archivo", required = false)
    @Parameter(name = "request", description = "HttpServletRequest", required = true)
    @ApiResponse(responseCode = "200", description = "Archivo borrado.")
    @ApiResponse(responseCode = "400", description = "Bad request.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = ["{filename:.+}"])
    @ResponseBody
    fun deleteFile(@PathVariable filename: String?, request: HttpServletRequest): ResponseEntity<Resource> =
        runBlocking {
            try {
                CoroutineScope(Dispatchers.IO).launch { storageService.delete(filename.toString()) }.join()
                ResponseEntity.ok().build()
            } catch (e: StorageException) {
                throw StorageExceptionBadRequest(e.message.toString())
            }
        }
}