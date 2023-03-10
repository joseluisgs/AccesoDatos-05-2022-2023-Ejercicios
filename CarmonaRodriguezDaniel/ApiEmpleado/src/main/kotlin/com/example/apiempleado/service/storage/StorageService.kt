package com.example.apiempleado.service.storage

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.util.stream.Stream

interface StorageService {
    fun init()
    fun store(file: MultipartFile): String
    fun load(filename: String): Path
    fun loadAsResource(filename: String): Resource
    fun delete(filename: String)
    fun getUrl(filename: String): String

    fun loadAll(): Stream<Path>
    fun deleteAll()
}