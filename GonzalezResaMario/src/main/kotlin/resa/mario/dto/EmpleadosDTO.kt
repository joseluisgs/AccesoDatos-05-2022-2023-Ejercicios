package resa.mario.dto

import kotlinx.serialization.Serializable

/**
 * DTO de empleado
 *
 * @property name
 * @property email
 * @property departamentoId
 * @property avatar
 */
@Serializable
data class EmpleadoDTO(
    val name: String,
    val email: String,
    val departamentoId: String? = null,
    val avatar: String? = null
)