package com.example.apiempleado.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "EMPLEADOS")
data class Empleado(
    @Id
    var id: Long? = null,
    var uuid: String = UUID.randomUUID().toString(),
    var name: String,
    var departamento: Long,
    var available: Boolean,
)
