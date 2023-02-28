package es.ruymi.departamentoempleadoruben.dto

import java.util.*

data class DepartamentoDTO(
    val id: String? = UUID.randomUUID().toString(),
    val nombre: String,
    val presupuesto: Double
)

