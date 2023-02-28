package mendoza.acosta.empresarestspringboot.service.storage

import mendoza.acosta.empresarestspringboot.controller.StorageController
import mendoza.acosta.empresarestspringboot.exception.StorageBadRequestException
import mendoza.acosta.empresarestspringboot.exception.StorageFileNotFoundException
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*
import java.util.stream.Stream

private val logger = KotlinLogging.logger {}

@Service
class StorageServiceFileSystemImpl(
    @Value("\${upload.root-location}") path: String,
    @Value("\${spring.profiles.active}") mode: String
) : StorageService {
    private val rootLocation: Path
    init {
        logger.info { "Inicializando Servicio de Almacenamiento de Ficheros" }
        rootLocation = Paths.get(path)
        if (mode == "dev") {
            this.deleteAll()
        }
        this.init()
    }

    override fun store(file: MultipartFile): String {
        logger.info { "Almacenando fichero: ${file.originalFilename}" }

        val filename = StringUtils.cleanPath(file.originalFilename.toString())
        val extension = StringUtils.getFilenameExtension(filename).toString()
        val justFilename = filename.replace(".$extension", "")
        val storedFilename = UUID.randomUUID().toString() + "." + extension
        try {
            if (file.isEmpty) {
                throw StorageBadRequestException("Fallo al almacenar un fichero vacío $filename")
            }
            if (filename.contains("..")) {
                throw StorageBadRequestException("No se puede almacenar un fichero fuera del path permitido $filename")
            }
            file.inputStream.use { inputStream ->
                Files.copy(
                    inputStream, rootLocation.resolve(storedFilename),
                    StandardCopyOption.REPLACE_EXISTING
                )
                return storedFilename
            }
        } catch (e: IOException) {
            throw StorageBadRequestException("Fallo al almacenar fichero $filename", e)
        }
    }

    override fun store(file: MultipartFile, filenameFromUser: String): String {
        logger.info { "Almacenando fichero: ${file.originalFilename}" }

        val filename = StringUtils.cleanPath(file.originalFilename.toString())
        val extension = StringUtils.getFilenameExtension(filename).toString()
        val justFilename = filename.replace(".$extension", "")
        val storedFilename = "$filenameFromUser.$extension"
        try {
            if (file.isEmpty) {
                throw StorageBadRequestException("Fallo al almacenar un fichero vacío $filename")
            }
            if (filename.contains("..")) {
                throw StorageBadRequestException("No se puede almacenar un fichero fuera del path permitido $filename")
            }
            file.inputStream.use { inputStream ->
                Files.copy(
                    inputStream, rootLocation.resolve(storedFilename),
                    StandardCopyOption.REPLACE_EXISTING
                )
                return storedFilename
            }
        } catch (e: IOException) {
            throw StorageBadRequestException("Fallo al almacenar fichero $filename", e)
        }
    }

    override fun loadAll(): Stream<Path> {
        logger.info { "Cargando todos los ficheros" }
        return try {
            Files.walk(rootLocation, 1)
                .filter { path -> !path.equals(rootLocation) }
                .map(rootLocation::relativize)
        } catch (e: IOException) {
            throw StorageBadRequestException("Fallo al leer los ficheros almacenados", e)
        }
    }

    override fun load(filename: String): Path {
        logger.info { "Cargando fichero: $filename" }
        return rootLocation.resolve(filename)
    }

    override fun loadAsResource(filename: String): Resource {
        logger.info { "Cargando fichero como recurso: $filename" }
        return try {
            val file = load(filename)
            val resource = UrlResource(file.toUri())
            if (resource.exists() || resource.isReadable) {
                resource
            } else {
                throw StorageFileNotFoundException(
                    "No se puede leer fichero: $filename"
                )
            }
        } catch (e: MalformedURLException) {
            throw StorageFileNotFoundException("No se puede leer fichero: $filename", e)
        }
    }

    override fun deleteAll() {
        logger.info { "Borrando todos los ficheros" }
        FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }


    override fun init() {
        logger.info { "Inicializando directorio de almacenamiento de ficheros" }
        try {
            if (!Files.exists(rootLocation))
                Files.createDirectory(rootLocation)
        } catch (e: IOException) {
            throw StorageBadRequestException("No se puede inicializar el sistema de almacenamiento", e)
        }
    }

    override fun delete(filename: String) {
        logger.info { "Borrando fichero: $filename" }
        val justFilename: String = StringUtils.getFilename(filename).toString()
        try {
            val file = load(justFilename)
            Files.deleteIfExists(file)
        } catch (e: IOException) {
            throw StorageBadRequestException("Error al eliminar un fichero", e)
        }
    }

    override fun getUrl(filename: String): String {
        logger.info { "Obteniendo URL de fichero: $filename" }

        return MvcUriComponentsBuilder
            .fromMethodName(StorageController::class.java, "serveFile", filename, null)
            .build().toUriString()
    }
}