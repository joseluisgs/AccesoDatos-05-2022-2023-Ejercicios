package es.ruymi.departamentoempleadoruben.exceptions

class StorageBadRequestException(message: String) : RuntimeException(message)
class StorageFileNotFoundException(message: String) : RuntimeException(message)
class StorageException(message: String) : RuntimeException(message)
