package com.example.apiempleado.config.websocket

interface WebSocketSender {
    fun sendMessage(message: String)
    fun sendPeriodicMessages()
}