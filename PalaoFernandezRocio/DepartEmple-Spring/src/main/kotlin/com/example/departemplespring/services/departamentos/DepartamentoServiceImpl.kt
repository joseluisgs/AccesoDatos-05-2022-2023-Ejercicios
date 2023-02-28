package com.example.departemplespring.services.departamentos

import com.example.departemplespring.exceptions.DepartamentoNotFoundException
import com.example.departemplespring.models.Departamento
import com.example.departemplespring.repositories.departamentos.DepartamentoCachedRepository
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class DepartamentoServiceImpl
@Autowired constructor(
    private val repository: DepartamentoCachedRepository
): DepartamentoService{

    override suspend fun findAll(): List<Departamento> {
        return repository.findAll().toList()
    }

    override suspend fun findById(id: Long): Departamento? {
        return repository.findById(id)
            ?: throw DepartamentoNotFoundException("No se ha encontrado un departamento con el id: $id")
    }

    override suspend fun findByUuid(uuid: UUID): Departamento? {
        return repository.findByUuid(uuid)
            ?: throw DepartamentoNotFoundException("No se ha encontrado un departamento con el uuid: $uuid")
    }

    override suspend fun save(item: Departamento): Departamento {
        return repository.save(item)
    }

    override suspend fun update(id: Long, item: Departamento): Departamento? {
        return repository.update(id, item)
            ?: throw DepartamentoNotFoundException("No se ha encontrado un departamento con el uuid: $id")
    }

    override suspend fun deleteById(id: Long): Departamento? {
        return repository.deleteById(id)
            ?: throw DepartamentoNotFoundException("No se ha encontrado un departamento con el id: $id")
    }
}