package es.ruymi.departamentoempleadoruben.repositories.empleados

import es.ruymi.departamentoempleadoruben.models.Empleado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class EmpleadosCachedRespositoryImpl
@Autowired constructor(
    private val empleadoRepository: EmpleadosRepository
): EmpleadosCachedRepository{



    override suspend fun findAll(): Flow<Empleado> = withContext(Dispatchers.IO) {
        return@withContext empleadoRepository.findAll()
    }

    override suspend fun findById(id: Long): Empleado? = withContext(Dispatchers.IO) {
        return@withContext empleadoRepository.findById(id)
    }

    override suspend fun findByUuid(uuid: UUID): Empleado? = withContext(Dispatchers.IO){
        return@withContext empleadoRepository.findByUuid(uuid).firstOrNull()
    }

    override suspend fun findByNombre(nombre: String): Flow<Empleado> = withContext(Dispatchers.IO){
        return@withContext empleadoRepository.findByNombreContainsIgnoreCase(nombre)
    }

    override suspend fun save(empleado: Empleado): Empleado = withContext(Dispatchers.IO){
        return@withContext empleadoRepository.save(empleado)
    }

    override suspend fun update(uuid: UUID, empleado: Empleado): Empleado? = withContext(Dispatchers.IO){
        val res = empleadoRepository.findByUuid(uuid).firstOrNull()
        res?.let {
            val resUpdate = it.copy(
                uuid = it.uuid,
                nombre = empleado.nombre,
                email = empleado.email,
                avatar = empleado.avatar,
                departamento_id = empleado.departamento_id
            )

            return@let empleadoRepository.save(resUpdate)
        }
        return@withContext null
    }

    override suspend fun delete(empleado: Empleado): Empleado? = withContext(Dispatchers.IO){
        val res = empleadoRepository.findByUuid(empleado.uuid).firstOrNull()
        res?.let {
            empleadoRepository.deleteById(it.id!!)
            return@withContext it
        }
        return@withContext null
    }

    override suspend fun deleteByUuid(uuid: UUID): Empleado? = withContext(Dispatchers.IO){
        val res = empleadoRepository.findByUuid(uuid).firstOrNull()
        res?.let {
            empleadoRepository.deleteById(it.id!!)
            return@withContext it
        }
        return@withContext null
    }

    override suspend fun deleteById(id: Long) = withContext(Dispatchers.IO){
        empleadoRepository.deleteById(id)
    }

    override suspend fun countAll(): Long = withContext(Dispatchers.IO){
        return@withContext empleadoRepository.count()
    }

    override suspend fun deleteAll() = withContext(Dispatchers.IO){
        empleadoRepository.deleteAll()
    }
}