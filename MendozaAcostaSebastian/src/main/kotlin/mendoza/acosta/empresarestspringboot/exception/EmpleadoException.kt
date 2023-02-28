package mendoza.acosta.empresarestspringboot.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

sealed class EmpleadoException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.NOT_FOUND)
class EmpleadoNotFoundException(message: String) : EmpleadoException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class EmpleadoBadRequestException(message: String) : EmpleadoException(message)