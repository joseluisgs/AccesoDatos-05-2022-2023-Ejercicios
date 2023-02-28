package daniel.rodriguez.ejerciciospring.services.storage

import daniel.rodriguez.ejerciciospring.controllers.StorageController
import daniel.rodriguez.ejerciciospring.exception.StorageExceptionBadRequest
import daniel.rodriguez.ejerciciospring.exception.StorageExceptionNotFound
import jakarta.servlet.http.HttpServletRequest
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
import java.util.stream.Stream

@Service
class StorageService(
    @Value("\${upload.root-location}") path: String,
): IStorageService {
    private val rootLocation: Path

    init {
        rootLocation = Paths.get(path)
        this.deleteAll()
        this.init()
    }

    override fun init() {
        try {
            if (!Files.exists(rootLocation)) Files.createDirectory(rootLocation)
        } catch (e: IOException) {
            throw StorageExceptionBadRequest("Unable to initialize storage system.", e)
        }
    }

    override suspend fun store(file: MultipartFile): String {
        val fileName = StringUtils.cleanPath(file.originalFilename.toString())
        val extension = StringUtils.getFilenameExtension(fileName).toString()
        val name = fileName.replace(".$extension", "")

        val storedName = "${name}_${System.currentTimeMillis()}.$extension"
        try {
            if (file.isEmpty)
                throw StorageExceptionBadRequest("Could not store $fileName. File is empty.")
            if (fileName.contains(".."))
                throw StorageExceptionBadRequest("You cannot store $fileName in a parent directory.")
            file.inputStream.use { input ->
                Files.copy(
                    input, rootLocation.resolve(storedName),
                    StandardCopyOption.REPLACE_EXISTING
                )
                return storedName
            }
        } catch (e: IOException) {
            throw StorageExceptionBadRequest("Could not store $fileName", e)
        }
    }

    override fun store(file: MultipartFile, fileName: String): String {
        val fileName = StringUtils.cleanPath(file.originalFilename.toString())
        val extension = StringUtils.getFilenameExtension(fileName).toString()

        val storedName = "$fileName.$extension"
        try {
            if (file.isEmpty)
                throw StorageExceptionBadRequest("Could not store $fileName. File is empty.")
            if (fileName.contains(".."))
                throw StorageExceptionBadRequest("You cannot store $fileName in a parent directory.")
            file.inputStream.use { inputStream ->
                Files.copy(
                    inputStream, rootLocation.resolve(storedName),
                    StandardCopyOption.REPLACE_EXISTING
                )
                return storedName
            }
        } catch (e: IOException) {
            throw StorageExceptionBadRequest("Could not store $fileName", e)
        }
    }

    override suspend fun loadAll(): Stream<Path> {
        return try {
            Files.walk(rootLocation, 1)
                .filter { path -> !path.equals(rootLocation) }
                .map(rootLocation::relativize)
        } catch (e: IOException) {
            throw StorageExceptionBadRequest("Could not read files", e)
        }
    }

    override suspend fun load(fileName: String): Path {
        return rootLocation.resolve(fileName)
    }

    override suspend fun loadAsResource(fileName: String): Resource {
        return try {
            val file = load(fileName)
            val resource = UrlResource(file.toUri())
            if (resource.exists() || resource.isReadable) resource
            else throw StorageExceptionNotFound("Unable to read $fileName")
        } catch (e: MalformedURLException) {
            throw StorageExceptionNotFound("Unable to read $fileName", e)
        }
    }

    override suspend fun delete(fileName: String) {
        val name: String = StringUtils.getFilename(fileName).toString()
        try {
            val file = load(name)
            Files.deleteIfExists(file)
        } catch (e: IOException) {
            throw StorageExceptionBadRequest("Unable to delete $fileName", e)
        }
    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }

    override suspend fun getUrl(fileName: String): String {
        return MvcUriComponentsBuilder
            .fromMethodName(StorageController::class.java, "serveFile", fileName, null)
            .build().toUriString()
    }
}