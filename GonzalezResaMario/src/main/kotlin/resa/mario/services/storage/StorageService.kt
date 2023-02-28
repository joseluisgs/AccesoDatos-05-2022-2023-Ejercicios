package resa.mario.services.storage

import org.springframework.web.multipart.MultipartFile

interface StorageService {

    fun initStorageDirectory()

    suspend fun saveFile(file: MultipartFile): String

    suspend fun saveFileFromUser(file: MultipartFile, filenameFromUser: String): String

    fun deleteAll()
}