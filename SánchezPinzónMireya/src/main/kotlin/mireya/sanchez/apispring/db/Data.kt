package mireya.sanchez.apispring.db

import mireya.sanchez.apispring.dto.UsuarioRegisterDTO
import mireya.sanchez.apispring.models.Usuario

fun getUsuariosInit() = listOf(
    UsuarioRegisterDTO(
        username = "Mireya",
        password = "mireya1234",
        rol = Usuario.Rol.ADMIN.name
    ),
    UsuarioRegisterDTO(
        username = "Pepe",
        password = "pepe1234",
        rol = Usuario.Rol.USER.name
    )
)