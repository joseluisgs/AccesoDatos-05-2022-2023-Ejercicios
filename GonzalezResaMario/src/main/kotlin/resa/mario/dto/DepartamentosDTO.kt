package resa.mario.dto

import kotlinx.serialization.Serializable

/**
 * DTO de departamento
 *
 * @property nombre
 * @property presupuesto
 */
@Serializable
data class DepartamentoDTO(
    val nombre: String,
    val presupuesto: Double
)