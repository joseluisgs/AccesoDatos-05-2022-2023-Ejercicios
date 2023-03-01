package es.ruymi.departamentoempleadoruben.services.storage

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.util.stream.Stream


interface StorageService {
    fun initStorageDirectory()
    suspend fun saveFile(file: MultipartFile): String
    suspend fun saveFileFromUser(file: MultipartFile, filenameFromUser: String): String
    fun deleteAll()
}
