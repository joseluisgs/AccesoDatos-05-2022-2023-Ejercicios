package daniel.rodriguez.ejerciciospring.mappers

import daniel.rodriguez.ejerciciospring.dto.UserDTO
import daniel.rodriguez.ejerciciospring.dto.UserDTOcreacion
import daniel.rodriguez.ejerciciospring.models.User

fun User.toDTO() = UserDTO(username, email, role)
fun UserDTOcreacion.fromDTO() = User(
    uuid = uuid,
    username = username,
    email = email,
    password = password,
    role = role
)