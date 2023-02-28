package resa.mario.db

import resa.mario.dto.UsuarioDTORegister
import resa.mario.models.Usuario

fun getUsersInit() = listOf(
    UsuarioDTORegister(
        username = "Mario111",
        password = "1234",
        role = Usuario.Role.ADMIN.name // "ADMIN"
    ),
    UsuarioDTORegister(
        username = "Alysys222",
        password = "1234",
        role = Usuario.Role.USER.name // "USER"
    )
)