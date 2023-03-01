package daniel.rodriguez.ejerciciospring.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

sealed class EmpleadoException(message: String?) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class EmpleadoExceptionBadRequest(message: String?) : EmpleadoException(message)

@ResponseStatus(HttpStatus.NOT_FOUND)
class EmpleadoExceptionNotFound(message: String?) : EmpleadoException(message)