package es.dam.springrest.services.storage

import es.dam.springrest.exceptions.StorageBadRequestException
import es.dam.springrest.exceptions.StorageException
import es.dam.springrest.exceptions.StorageNotFoundException
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

@Service
class StorageService(
    @Value("\${upload.root-location}") path: String
): IStorageService {
    private val ruta = Paths.get(path)

    init {
        initDirectory()
    }

    final override fun initDirectory() {
        if (!Files.exists(ruta)) {
            Files.createDirectory(ruta)
        }
    }

    override suspend fun saveFile(file: MultipartFile): String {
        val filename = StringUtils.cleanPath(file.originalFilename.toString())
        val extension = StringUtils.getFilenameExtension(filename).toString()
        val justFilename = filename.replace(".$extension", "")
        val storedFilename = System.currentTimeMillis().toString() + "_" + justFilename + "." + extension

        try {
            if (file.isEmpty) {
                throw StorageBadRequestException("Error al guardar el fichero $filename")
            }
            if (filename.contains("..")) {
                throw StorageBadRequestException("Error al guardar el fichero $filename")
            }
            file.inputStream.use { inputStream ->
                Files.copy(
                    inputStream, ruta.resolve(storedFilename),
                    StandardCopyOption.REPLACE_EXISTING
                )
                return storedFilename
            }
        } catch (e: IOException) {
            throw StorageBadRequestException("Error al guardar el fichero $filename", e)
        }
    }

    override suspend fun getFile(file: String): Resource {
        return try {
            val fichero = ruta.resolve(file)
            val resource = UrlResource(fichero.toUri())
            if (resource.exists() || resource.isReadable) {
                resource
            } else {
                throw StorageNotFoundException(
                    "Imposible leer fichero: $file"
                )
            }
        } catch (e: MalformedURLException) {
            throw StorageNotFoundException("Imposible leer fichero: $file", e)
        }
    }
}