package com.example.apiempleado.dto

import com.example.apiempleado.model.Departamento

data class EmpleadoCreateDto(
    val name: String,
    val departamentoId: Long,
)

data class EmpleadoUpdateDto(
    val name: String,
    val available: Boolean,
)

data class EmpleadoDto(
    val id: Long,
    val name: String,
    val departamento: Departamento,
)