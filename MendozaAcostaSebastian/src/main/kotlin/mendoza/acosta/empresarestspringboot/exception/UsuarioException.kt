package mendoza.acosta.empresarestspringboot.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

sealed class UsuarioException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.NOT_FOUND)
class UsuarioNotFoundException(message: String) : UsuarioException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class UsuarioBadRequestException(message: String) : UsuarioException(message)