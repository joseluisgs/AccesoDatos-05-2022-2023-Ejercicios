package daniel.rodriguez.ejerciciospring.services.storage

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.util.stream.Stream

interface IStorageService {
    fun init()
    suspend fun store(file: MultipartFile): String
    suspend fun loadAll(): Stream<Path>
    suspend fun load(fileName: String): Path
    suspend fun loadAsResource(fileName: String): Resource
    suspend fun delete(fileName: String)
    fun deleteAll()
    suspend fun getUrl(fileName: String): String
    fun store(file: MultipartFile, fileName: String): String
}