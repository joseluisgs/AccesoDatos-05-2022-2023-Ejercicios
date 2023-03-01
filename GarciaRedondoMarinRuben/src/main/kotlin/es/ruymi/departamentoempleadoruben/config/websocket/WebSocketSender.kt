package es.ruymi.departamentoempleadoruben.config.websocket

interface WebSocketSender {
    fun sendMessage(message: String)
    fun sendPeriodicMessages()
}