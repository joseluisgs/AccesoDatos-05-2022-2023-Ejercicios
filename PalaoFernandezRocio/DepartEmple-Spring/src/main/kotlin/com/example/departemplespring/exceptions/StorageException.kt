package com.example.departemplespring.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

sealed class StorageException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class StorageBadResquestException(message: String) : StorageException(message)
@ResponseStatus(HttpStatus.NOT_FOUND)
class StorageFileNotFoundException(message: String): StorageException(message)