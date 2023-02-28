package com.example.departemplespring.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "empleados")
data class Empleado(
    @Id
    var id: Long? = null,
    var nombre: String,
    var email: String,
//    var username: String,
//    var password: String,
//    var avatar: String?,
    var uuidDepartamento: UUID,
//    var rol: Rol
)
