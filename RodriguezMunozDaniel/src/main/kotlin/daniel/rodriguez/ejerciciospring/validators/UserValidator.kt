package daniel.rodriguez.ejerciciospring.validators

import daniel.rodriguez.ejerciciospring.dto.UserDTOcreacion
import daniel.rodriguez.ejerciciospring.dto.UserDTOlogin
import daniel.rodriguez.ejerciciospring.exception.UserExceptionBadRequest

fun UserDTOcreacion.validate(): UserDTOcreacion {
    if (this.username.isBlank())
        throw UserExceptionBadRequest("Username cannot be blank.")
    else if (this.email.isBlank())
        throw UserExceptionBadRequest("Email cannot be blank.")
    else if (!this.email.matches(Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")))
        throw UserExceptionBadRequest("Invalid email.")
    else if (this.password.length < 7 || this.password.isBlank())
        throw UserExceptionBadRequest("Password must at least be 7 characters long.")
    else return this
}

fun UserDTOlogin.validate(): UserDTOlogin {
    if (this.username.isBlank())
        throw UserExceptionBadRequest("Username cannot be blank.")
    else if (this.password.length < 7 || this.password.isBlank())
        throw UserExceptionBadRequest("Password must at least be 7 characters long.")
    else return this
}