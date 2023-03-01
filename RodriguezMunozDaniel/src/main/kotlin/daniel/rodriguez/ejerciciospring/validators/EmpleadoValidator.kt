package daniel.rodriguez.ejerciciospring.validators

import daniel.rodriguez.ejerciciospring.dto.EmpleadoDTOcreacion
import daniel.rodriguez.ejerciciospring.exception.EmpleadoExceptionBadRequest

fun EmpleadoDTOcreacion.validate(): EmpleadoDTOcreacion {
    if (this.nombre.isBlank())
        throw EmpleadoExceptionBadRequest("Nombre cannot be blank.")
    else if (this.email.isBlank())
        throw EmpleadoExceptionBadRequest("Email cannot be blank.")
    else if (!this.email.matches(Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")))
        throw EmpleadoExceptionBadRequest("Invalid email.")
    else return this
}