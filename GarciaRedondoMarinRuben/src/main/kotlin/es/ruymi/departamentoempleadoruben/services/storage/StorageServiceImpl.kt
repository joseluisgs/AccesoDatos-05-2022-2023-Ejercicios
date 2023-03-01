package es.ruymi.departamentoempleadoruben.services.storage

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


@Service
class StorageServiceImpl(
    @Value("\${upload.root-location}") path: String,
) : StorageService {
    private val rootLocation: Path = Paths.get(path)

    init {
        initStorageDirectory()
    }

    final override fun initStorageDirectory() {
        if (!Files.exists(rootLocation)) {
            Files.createDirectory(rootLocation)
        } else {
            deleteAll()
            Files.createDirectory(rootLocation)
        }
    }

    override suspend fun saveFile(file: MultipartFile): String {
        val extension = StringUtils.getFilenameExtension(file.originalFilename)
        val fileSaved = UUID.randomUUID().toString() + "." + extension
        try {
            if (file.isEmpty) {
                throw Exception("Fallo al almacenar un fichero vacío ${file.originalFilename}")
            }
            if (fileSaved.contains("..")) {
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
        val extension = StringUtils.getFilenameExtension(file.originalFilename)
        val fileSaved = "$filenameFromUser.$extension"
        try {
            if (file.isEmpty) {
                throw Exception("Fallo al almacenar un fichero vacío ${file.originalFilename}")
            }
            if (fileSaved.contains("..")) {
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
        FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }

}