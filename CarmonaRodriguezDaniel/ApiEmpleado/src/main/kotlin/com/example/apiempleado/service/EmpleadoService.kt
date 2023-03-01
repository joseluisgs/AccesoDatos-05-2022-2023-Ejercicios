package com.example.apiempleado.service

import com.example.apiempleado.config.websocket.WebSocketConfig
import com.example.apiempleado.config.websocket.WebSocketHandler
import com.example.apiempleado.dto.EmpleadoUpdateDto
import com.example.apiempleado.exception.DepartamentoNotFoundException
import com.example.apiempleado.exception.EmpleadoNotFoundException
import com.example.apiempleado.mapper.toEmpleado
import com.example.apiempleado.model.Departamento
import com.example.apiempleado.model.Empleado
import com.example.apiempleado.model.Notification
import com.example.apiempleado.repository.departamento.DepartamentoCacheRepository
import com.example.apiempleado.repository.empleado.EmpleadoCacheRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EmpleadoService
@Autowired constructor(
    private val repository: EmpleadoCacheRepository,
    private val repoDepartamento: DepartamentoCacheRepository,
    private val webSocketConfig: WebSocketConfig,
) {
    private val webSocketService = webSocketConfig.webSocketEmpleadoHandler() as WebSocketHandler

    suspend fun findAll(): Flow<Empleado> {
        return repository.findAll()
    }

    suspend fun findById(id: Long): Empleado {
        return repository.findById(id)
            ?: throw EmpleadoNotFoundException("No existe el empleado con id: $id")
    }

    suspend fun save(empleado: Empleado): Empleado {
        findDepartamento(empleado.departamento!!)

        return repository.save(empleado).also {
            onChange(Notification.Tipo.CREATE, it.id!!, it)
        }
    }

    suspend fun update(id: Long, empleado: EmpleadoUpdateDto): Empleado {
        val existe = repository.findById(id)

        existe?.let {
            val depart = findDepartamento(it.departamento!!)
            return repository.update(id, empleado.toEmpleado(depart))
                ?.also {
                    onChange(Notification.Tipo.UPDATE, it.id!!, it)
                }!!
        } ?: throw EmpleadoNotFoundException("No existe el empleado con id: $id")
    }

    suspend fun delete(empleado: Empleado): Boolean {
        val existe = repository.findById(empleado.id!!)

        existe?.let {
            return repository.delete(existe)
                .also {
                    onChange(Notification.Tipo.DELETE, existe.id!!, existe)
                }
        } ?: throw EmpleadoNotFoundException("No existe el empleado con id: ${empleado.id}")
    }

    suspend fun deleteById(id: Long) {
        repository.deleteById(id)
    }

    suspend fun countAll(): Long {
        return repository.countAll()
    }

    suspend fun findDepartamento(id: Long): Departamento {
        return repoDepartamento.findById(id)
            ?: throw DepartamentoNotFoundException("No existe el departamento: $id")
    }

    suspend fun onChange(tipo: Notification.Tipo, id: Long, data: Empleado?) {
        val mapper = jacksonObjectMapper()
        val json = mapper.writeValueAsString(
            Notification<Empleado>(
                "EMPLEADO",
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