package es.dam.springrest.dto

data class EmpleadoResponseDTO(
    val nombre: String,
    val email: String,
    val avatar: String,
    val departamento_id: Long
)