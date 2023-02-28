package mendoza.acosta.empresarestspringboot.mappers

import mendoza.acosta.empresarestspringboot.dto.UsuarioCreateDto
import mendoza.acosta.empresarestspringboot.dto.UsuarioDto
import mendoza.acosta.empresarestspringboot.models.Usuario

fun Usuario.toDto(): UsuarioDto {
    return UsuarioDto(
        nombre = this.nombre,
        username = this.username,
        password = this.password,
        email = this.email,
        avatar = this.avatar,
        rol = this.rol
    )
}

fun UsuarioCreateDto.toModel(): Usuario {
    return Usuario(
        nombre = this.nombre,
        username = this.username,
        password = this.password,
        email = this.email,
        avatar = this.avatar ?: "https://upload.wikimedia.org/wikipedia/commons/f/f4/User_Avatar_2.png",
        rol = this.rol
    )
}