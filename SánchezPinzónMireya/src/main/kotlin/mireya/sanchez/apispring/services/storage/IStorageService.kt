package mireya.sanchez.apispring.services.storage

import org.springframework.web.multipart.MultipartFile

interface IStorageService {
    fun initStorageDirectory()

    suspend fun saveFile(file: MultipartFile): String

    suspend fun saveFileFromUser(file: MultipartFile, filenameFromUser: String): String

    fun deleteAll()
}