package com.example.apiempleado.service.storage

import com.example.apiempleado.controller.StorageController
import com.example.apiempleado.exception.StorageBadRequestException
import com.example.apiempleado.exception.StorageFileNotFoundException
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

@Service
class StorageServiceImpl(
    @Value("\${upload.root-location}") path: String,
) : StorageService {
    private val rootPath: Path

    init {
        rootPath = Paths.get(path)
        this.init()
    }

    override fun init() {
        if (!Files.exists(rootPath)) {
            Files.createDirectory(rootPath)
        }
    }

    override fun store(file: MultipartFile): String {
        val filename = StringUtils.cleanPath(file.originalFilename.toString())
        val extension = StringUtils.getFilenameExtension(filename).toString()
        val fileNameStorage = UUID.randomUUID().toString() + "." + extension

        if (file.isEmpty) {
            throw StorageBadRequestException("El fichero esta vacío")
        }
        if (filename.contains("..")) {
            throw StorageBadRequestException("No se puede almacenar un fichero fuera del path permitido $filename")
        }
        file.inputStream.use { input ->
            Files.copy(
                input, rootPath.resolve(fileNameStorage),
                StandardCopyOption.REPLACE_EXISTING
            )
            return fileNameStorage
        }
    }


    override fun loadAll(): Stream<Path> {
        return try {
            Files.walk(rootPath, 1)
                .filter { path -> !path.equals(rootPath) }
                .map(rootPath::relativize)
        } catch (e: IOException) {
            throw StorageBadRequestException("Fallo al leer los ficheros almacenados")
        }
    }

    override fun load(filename: String): Path {
        return rootPath.resolve(filename)
    }

    override fun loadAsResource(filename: String): Resource {
        try {
            val file = load(filename)
            println(file)
            val resource = UrlResource(file.toUri())
            if (resource.exists() || resource.isReadable) {
                return resource
            } else {
                throw StorageFileNotFoundException(
                    "No se puede leer fichero: $filename"
                )
            }
        } catch (e: MalformedURLException) {
            throw StorageFileNotFoundException("No se puede leer fichero: $filename")
        }

    }

    override fun delete(filename: String) {
        val fileName = StringUtils.getFilename(filename).toString()
        val file = load(fileName)
        Files.deleteIfExists(file)
    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootPath.toFile())
    }

    override fun getUrl(filename: String): String {
        return MvcUriComponentsBuilder.fromMethodName(StorageController::class.java, "getFile", filename, null)
            .build().toUriString()
    }
}