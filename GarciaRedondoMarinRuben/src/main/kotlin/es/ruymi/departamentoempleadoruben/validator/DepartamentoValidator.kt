package es.ruymi.departamentoempleadoruben.validator

import es.ruymi.departamentoempleadoruben.dto.DepartamentoDTO
import es.ruymi.departamentoempleadoruben.exceptions.DepartamentoBadRequestException


fun DepartamentoDTO.validate(): DepartamentoDTO{
    if(this.nombre.isBlank()){
        throw DepartamentoBadRequestException("El nombre del departamento no puede estar vacío")
    } else if(this.presupuesto <= 0.0){
        throw DepartamentoBadRequestException("El presupuesto del departamento debe ser mayor a 0")
    }
    return this
}