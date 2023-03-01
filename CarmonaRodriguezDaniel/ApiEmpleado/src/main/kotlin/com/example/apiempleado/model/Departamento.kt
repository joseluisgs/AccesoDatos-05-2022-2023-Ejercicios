package com.example.apiempleado.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "DEPARTAMENTOS")
data class Departamento(
    @Id
    var id: Long? = null,
    var uuid: String = UUID.randomUUID().toString(),
    var name: String,
)
