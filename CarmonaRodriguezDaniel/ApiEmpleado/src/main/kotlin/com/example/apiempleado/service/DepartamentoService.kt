package com.example.apiempleado.service

import com.example.apiempleado.config.websocket.WebSocketConfig
import com.example.apiempleado.config.websocket.WebSocketHandler
import com.example.apiempleado.exception.DepartamentoNotFoundException
import com.example.apiempleado.model.Departamento
import com.example.apiempleado.model.Empleado
import com.example.apiempleado.model.Notification
import com.example.apiempleado.repository.departamento.DepartamentoCacheRepository
import com.example.apiempleado.repository.empleado.EmpleadoCacheRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DepartamentoService
@Autowired constructor(
    private val repository: DepartamentoCacheRepository,
    private val repEmpleado: EmpleadoCacheRepository,
    private val webSocketConfig: WebSocketConfig,
) {
    private val webSocketService = webSocketConfig.webSocketDepartamentoHandler() as WebSocketHandler

    suspend fun findAll(): Flow<Departamento> {
        return repository.findAll()
    }

    suspend fun findById(id: Long): Departamento {
        return repository.findById(id)
            ?: throw DepartamentoNotFoundException("No existe el departamento con id: $id")
    }

    suspend fun save(departamento: Departamento): Departamento {
        return repository.save(departamento).also {
            onChange(Notification.Tipo.CREATE, it.id!!, it)
        }
    }

    suspend fun update(id: Long, departamento: Departamento): Departamento {
        val existe = repository.findById(id)

        existe?.let {
            return repository.update(id, departamento)
                ?.also {
                    onChange(Notification.Tipo.UPDATE, it.id!!, it)
                }!!
        } ?: throw DepartamentoNotFoundException("No existe el departamento con id: $id")
    }

    suspend fun delete(departamento: Departamento): Boolean {
        val existe = repository.findById(departamento.id!!)

        existe?.let {
            return repository.delete(existe)
                .also {
                    onChange(Notification.Tipo.DELETE, existe.id!!, existe)
                }
        } ?: throw DepartamentoNotFoundException("No existe el departamento con id: ${departamento.id}")
    }

    suspend fun deleteById(id: Long) {
        repository.deleteById(id)
    }

    suspend fun countAll(): Long {
        return repository.countAll()
    }

    suspend fun findByEmpleado(id: Long): Flow<Empleado> {
        return repEmpleado.findAll().filter { it.departamento == id }
    }

    suspend fun onChange(tipo: Notification.Tipo, id: Long, data: Departamento?) {
        val mapper = jacksonObjectMapper()
        val json = mapper.writeValueAsString(
            Notification<Departamento>(
                "DEPARTAMENTO",
                tipo,
                id,
                data!!
            )
        )
        val myScope = CoroutineScope(Dispatchers.IO)
        myScope.launch {
            webSocketService.sendMessage(json)
        }
    }
}