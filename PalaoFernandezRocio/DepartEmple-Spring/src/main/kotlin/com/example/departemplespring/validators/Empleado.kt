package com.example.departemplespring.validators

import com.example.departemplespring.dto.EmpleadoCreateDto
import com.example.departemplespring.exceptions.EmpleadoBadRequestException

fun EmpleadoCreateDto.validarCreate():EmpleadoCreateDto{
    if(this.nombre.isBlank())
        throw EmpleadoBadRequestException("El nombre no puede estar vacío")
    if(this.email.isBlank())
        throw EmpleadoBadRequestException("El email no puede estar vacío")
    if(!this.email.matches(Regex("^[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\$")))
        throw EmpleadoBadRequestException("El email tiene un formato incorrecto")
    if(this.uuidDepartamento.isBlank())
        throw EmpleadoBadRequestException("El uuid del departamento no puede estar vacío")
    return this
}