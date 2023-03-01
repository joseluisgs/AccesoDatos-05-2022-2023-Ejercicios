package daniel.rodriguez.ejerciciospring.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

sealed class DepartamentoException(message: String?) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class DepartamentoExceptionBadRequest(message: String?) : DepartamentoException(message)

@ResponseStatus(HttpStatus.NOT_FOUND)
class DepartamentoExceptionNotFound(message: String?) : DepartamentoException(message)