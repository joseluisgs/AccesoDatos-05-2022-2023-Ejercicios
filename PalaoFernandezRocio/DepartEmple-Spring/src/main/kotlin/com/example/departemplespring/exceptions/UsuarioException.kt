package com.example.departemplespring.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

sealed class UsuarioException(message: String): RuntimeException(message)
@ResponseStatus(HttpStatus.NOT_FOUND)
class UsuarioNotFoundException(message: String) : UsuarioException(message)
class UsuarioBadRequestException(message: String) : UsuarioException(message)