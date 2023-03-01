package com.example.apiempleado.exception

sealed class StorageException(message: String) : RuntimeException(message)

class StorageBadRequestException(message: String) : StorageException(message)
class StorageFileNotFoundException(message: String) : StorageException(message)