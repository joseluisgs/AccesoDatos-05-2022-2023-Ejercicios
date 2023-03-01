package daniel.rodriguez.ejerciciospring.validators

import daniel.rodriguez.ejerciciospring.dto.DepartamentoDTOcreacion
import daniel.rodriguez.ejerciciospring.exception.DepartamentoExceptionBadRequest

fun DepartamentoDTOcreacion.validate(): DepartamentoDTOcreacion {
    if (this.nombre.isBlank())
        throw DepartamentoExceptionBadRequest("Nombre cannot be blank.")
    else if (this.presupuesto < 0)
        throw DepartamentoExceptionBadRequest("Presupuesto must be a positive number.")
    else return this
}