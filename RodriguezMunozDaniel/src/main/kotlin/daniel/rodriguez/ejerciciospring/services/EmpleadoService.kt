package daniel.rodriguez.ejerciciospring.services

import daniel.rodriguez.ejerciciospring.controllers.StorageController
import daniel.rodriguez.ejerciciospring.dto.EmpleadoDTO
import daniel.rodriguez.ejerciciospring.dto.EmpleadoDTOcreacion
import daniel.rodriguez.ejerciciospring.exception.DepartamentoExceptionNotFound
import daniel.rodriguez.ejerciciospring.exception.EmpleadoExceptionNotFound
import daniel.rodriguez.ejerciciospring.exception.StorageExceptionNotFound
import daniel.rodriguez.ejerciciospring.mappers.fromDTO
import daniel.rodriguez.ejerciciospring.mappers.toDTO
import daniel.rodriguez.ejerciciospring.models.Empleado
import daniel.rodriguez.ejerciciospring.repositories.departamento.DepartamentoRepositoryCached
import daniel.rodriguez.ejerciciospring.repositories.empleado.EmpleadoRepositoryCached
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile
import java.util.*


@Service
class EmpleadoService
@Autowired constructor(
    private val repo: EmpleadoRepositoryCached,
    private val dRepo: DepartamentoRepositoryCached
) {
    suspend fun findEmpleadoByUuid(uuid: UUID): Empleado = withContext(Dispatchers.IO) {
        repo.findByUUID(uuid)
            ?: throw EmpleadoExceptionNotFound("Couldn't find empleado with uuid $uuid.")
    }

    suspend fun findAllEmpleados(): List<EmpleadoDTO> = withContext(Dispatchers.IO) {
        repo.findAll().toList().map { it.toDTO() }
    }

    suspend fun saveEmpleado(entity: EmpleadoDTOcreacion): EmpleadoDTO = withContext(Dispatchers.IO) {
        dRepo.findByUUID(entity.departamentoId)
            ?: throw DepartamentoExceptionNotFound("Couldn't find departamento with uuid ${entity.departamentoId}.")
        repo.save(entity.fromDTO()).toDTO()
    }

    suspend fun deleteEmpleado(id: UUID): EmpleadoDTO? = withContext(Dispatchers.IO) {
        val entity = repo.findByUUID(id)
            ?: throw EmpleadoExceptionNotFound("Empleado with uuid $id not found.")

        entity.id?.let { repo.delete(it) }
        entity.toDTO()
    }

    suspend fun updateAvatar(empleado: Empleado, file: MultipartFile): Empleado = withContext(Dispatchers.IO) {
        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA
        val body: MultiValueMap<String, Any> = LinkedMultiValueMap()
        body.add("file", file.resource)
        val requestEntity = HttpEntity(body, headers)
        val uri = "http://localhost:8080/ejercicioSpring/storage"
        val response = RestTemplate().postForEntity(uri, requestEntity, Map::class.java)
        val avatarUrl = response.body?.get("url")?.toString()
            ?: throw StorageExceptionNotFound("Url not found.")

        val empleadoUpdated = Empleado(
            id = empleado.id,
            uuid = empleado.uuid,
            nombre = empleado.nombre,
            email = empleado.email,
            avatar = avatarUrl,
            departamentoId = empleado.departamentoId
        )

        repo.save(empleadoUpdated)
    }
}