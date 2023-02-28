package com.example.departemplespring.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name ="departamentos")
data class Departamento(
    @Id
    var id: Long? = null,
    var uuid: UUID = UUID.randomUUID(),
    val nombre: String,
    val presupuesto: Float
)



