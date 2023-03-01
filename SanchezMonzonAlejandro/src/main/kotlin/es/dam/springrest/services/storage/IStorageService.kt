package es.dam.springrest.services.storage

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile

interface IStorageService {
    fun initDirectory()
    suspend fun saveFile(file: MultipartFile): String
    suspend fun getFile(file: String): Resource
}