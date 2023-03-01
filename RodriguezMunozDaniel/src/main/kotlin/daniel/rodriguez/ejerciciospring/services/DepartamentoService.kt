package daniel.rodriguez.ejerciciospring.services

import daniel.rodriguez.ejerciciospring.dto.DepartamentoDTO
import daniel.rodriguez.ejerciciospring.dto.DepartamentoDTOcreacion
import daniel.rodriguez.ejerciciospring.exception.DepartamentoExceptionBadRequest
import daniel.rodriguez.ejerciciospring.exception.DepartamentoExceptionNotFound
import daniel.rodriguez.ejerciciospring.mappers.fromDTO
import daniel.rodriguez.ejerciciospring.mappers.toDTO
import daniel.rodriguez.ejerciciospring.repositories.departamento.DepartamentoRepositoryCached
import daniel.rodriguez.ejerciciospring.repositories.empleado.EmpleadoRepositoryCached
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class DepartamentoService
@Autowired constructor(
    private val repo: DepartamentoRepositoryCached,
    private val eRepo: EmpleadoRepositoryCached
) {
    suspend fun findDepartamentoByUuid(uuid: UUID): DepartamentoDTO = withContext(Dispatchers.IO) {
        val empleados = eRepo.findAll().filter { it.departamentoId == uuid }.toSet()
        repo.findByUUID(uuid)?.toDTO(empleados)
            ?: throw DepartamentoExceptionNotFound("Couldn't find departamento with uuid $uuid.")
    }

    suspend fun findAllDepartamentos(): List<DepartamentoDTO> = withContext(Dispatchers.IO) {
        repo.findAll().toList().map { dep ->
            val empleados = eRepo.findAll().filter { emp ->
                emp.departamentoId == dep.uuid }.toSet()
            dep.toDTO(empleados)
        }
    }

    suspend fun saveDepartamento(entity: DepartamentoDTOcreacion): DepartamentoDTO = withContext(Dispatchers.IO) {
        val empleados = eRepo.findAll().filter { it.departamentoId == entity.id }.toSet()
        repo.save(entity.fromDTO()).toDTO(empleados)
    }

    suspend fun deleteDepartamento(id: UUID): DepartamentoDTO? = withContext(Dispatchers.IO) {
        val entity = repo.findByUUID(id)
            ?: throw DepartamentoExceptionNotFound("Departamento with uuid $id not found.")

        val empleados = eRepo.findAll().filter { it.departamentoId == entity.uuid }.toSet()
        if (empleados.isNotEmpty())
            throw DepartamentoExceptionBadRequest("Cannot delete departamento. It has empleados attached to it.")
        entity.id?.let { repo.delete(it) }
        entity.toDTO(empleados)
    }
}