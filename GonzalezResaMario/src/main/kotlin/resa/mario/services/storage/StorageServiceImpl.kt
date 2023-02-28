package resa.mario.services.storage

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

private val log = KotlinLogging.logger {}

/**
 * Servicio de almacenamiento
 *
 * @constructor
 * Ruta leida del achivo de propiedades
 *
 * @param path
 */
@Service
class StorageServiceImpl(
    @Value("\${upload.root-location}") path: String
) : StorageService {

    private val rootLocation: Path = Paths.get(path)

    init {
        initStorageDirectory()
    }

    final override fun initStorageDirectory() {
        if (!Files.exists(rootLocation)) {
            log.info { "Creando directorio de almacenamiento: $rootLocation" }
            Files.createDirectory(rootLocation)
        } else {
            log.debug { "El directorio existe; eliminacion de datos en proceso..." }
            deleteAll()
            // Volvemos a crear el directorio una vez se han eliminado los datos (eso incluye al propio directorio)
            Files.createDirectory(rootLocation)
        }
    }

    override suspend fun saveFile(file: MultipartFile): String {
        log.info { "Almacenando fichero: ${file.originalFilename}" }

        val extension = StringUtils.getFilenameExtension(file.originalFilename)
        val fileSaved = UUID.randomUUID().toString() + "." + extension

        try {
            if (file.isEmpty) {
                throw Exception("Fallo al almacenar un fichero vacío ${file.originalFilename}")
            }
            if (fileSaved.contains("..")) {
                // This is a security check
                throw Exception("No se puede almacenar un fichero fuera del path permitido ${file.originalFilename}")
            }
            file.inputStream.use { inputStream ->
                Files.copy(
                    inputStream, rootLocation.resolve(fileSaved),
                    StandardCopyOption.REPLACE_EXISTING
                )
                return fileSaved
            }
        } catch (e: IOException) {
            throw Exception("Fallo al almacenar fichero ${file.originalFilename}", e)
        }
    }

    override suspend fun saveFileFromUser(file: MultipartFile, filenameFromUser: String): String {
        log.info { "Almacenando fichero: ${file.originalFilename}" }

        val extension = StringUtils.getFilenameExtension(file.originalFilename)
        val fileSaved = "$filenameFromUser.$extension"

        try {
            if (file.isEmpty) {
                throw Exception("Fallo al almacenar un fichero vacío ${file.originalFilename}")
            }
            if (fileSaved.contains("..")) {
                // This is a security check
                throw Exception("No se puede almacenar un fichero fuera del path permitido ${file.originalFilename}")
            }
            file.inputStream.use { inputStream ->
                Files.copy(
                    inputStream, rootLocation.resolve(fileSaved),
                    StandardCopyOption.REPLACE_EXISTING
                )
                return fileSaved
            }
        } catch (e: IOException) {
            throw Exception("Fallo al almacenar fichero ${file.originalFilename}", e)
        }
    }


    override fun deleteAll() {
        log.info { "Eliminando ficheros del directorio de almacenamiento: $rootLocation" }
        FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }

}