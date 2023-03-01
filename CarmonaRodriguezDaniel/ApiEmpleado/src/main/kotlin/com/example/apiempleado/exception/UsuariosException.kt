package com.example.apiempleado.exception

sealed class UsuarioException(message: String) : RuntimeException(message)

class UsuarioNotFoundException(message: String) : UsuarioException(message)

class UsuarioBadRequestException(message: String) : UsuarioException(message)