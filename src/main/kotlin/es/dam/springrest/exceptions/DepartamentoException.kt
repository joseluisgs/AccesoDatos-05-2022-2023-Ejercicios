package es.dam.springrest.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

sealed class DepartamentoException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.NOT_FOUND)
class DepartamentoNotFoundException(message: String) : DepartamentoException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class DepartamentoBadRequestException(message: String) : DepartamentoException(message)