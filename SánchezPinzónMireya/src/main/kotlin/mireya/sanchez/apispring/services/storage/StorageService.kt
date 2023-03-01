package mireya.sanchez.apispring.services.storage

import mireya.sanchez.apispring.exceptions.StorageBadRequestException
import org.springframework.util.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

@Service
class StorageService(
    @Value("\${upload.root-location}")
    val path: String
) : IStorageService{
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
                throw StorageBadRequestException("Empty file ${file.originalFilename}")
            }
            if (fileSaved.contains("..")) {
                throw StorageBadRequestException("Path not permitted ${file.originalFilename}")
            }
            file.inputStream.use { inputStream ->
                Files.copy(
                    inputStream, rootLocation.resolve(fileSaved),
                    StandardCopyOption.REPLACE_EXISTING
                )
                return fileSaved
            }
        } catch (e: IOException) {
            throw StorageBadRequestException("Error saving ${file.originalFilename}")
        }
    }

    override suspend fun saveFileFromUser(file: MultipartFile, filenameFromUser: String): String {
        val extension = StringUtils.getFilenameExtension(file.originalFilename)
        val fileSaved = "$filenameFromUser.$extension"

        try {
            if (file.isEmpty) {
                throw StorageBadRequestException("Empty file ${file.originalFilename}")            }
            if (fileSaved.contains("..")) {
                throw StorageBadRequestException("Path not permitted ${file.originalFilename}")
            }
            file.inputStream.use { inputStream ->
                Files.copy(
                    inputStream, rootLocation.resolve(fileSaved),
                    StandardCopyOption.REPLACE_EXISTING
                )
                return fileSaved
            }
        } catch (e: IOException) {
            throw StorageBadRequestException("Error saving ${file.originalFilename}")
        }
    }


    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }
}