package es.ruymi.departamentoempleadoruben.validator

import es.ruymi.departamentoempleadoruben.dto.EmpleadoDTO
import es.ruymi.departamentoempleadoruben.exceptions.EmpleadoBadRequestException

fun EmpleadoDTO.validate(): EmpleadoDTO{
    if(this.nombre.isBlank()){
        throw EmpleadoBadRequestException("El nombre del empleado no puede estar en blanco")
    }else if(this.email.isBlank()){
        throw EmpleadoBadRequestException("El email del empleado no puede estar en blanco")
    }else if(this.avatar.isBlank()){
        throw EmpleadoBadRequestException("El avatar del empleado no puede estar en blanco")
    }
    return this
}