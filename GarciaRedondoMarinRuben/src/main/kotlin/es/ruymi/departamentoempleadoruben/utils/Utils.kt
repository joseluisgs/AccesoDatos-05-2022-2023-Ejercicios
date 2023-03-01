package es.ruymi.departamentoempleadoruben.utils

import es.ruymi.departamentoempleadoruben.exceptions.UUIDException
import java.util.*


fun String.toUUID(): UUID {
    return try {
        UUID.fromString(this)
    } catch (e: IllegalArgumentException) {
        throw UUIDException("El uuid no es válido o no está en el formato UUID")
    }
}