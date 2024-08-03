package com.tre3p.it

import com.tre3p.BaseIntegrationTest
import com.tre3p.resp.types.BulkString
import com.tre3p.resp.types.RESPArray
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PingHandlerTest : BaseIntegrationTest() {

    @Test
    fun shouldHandleSeveralCommandsFromSameConnection() {
        val clientConn = getServerConnection()
        val pingMessage = respEncoder.encode(RESPArray(listOf(BulkString("PING"))))

        clientConn.sendServerMessage(pingMessage)
        val firstServerResponse = clientConn.awaitServerMessage()

        Assertions.assertNotNull(firstServerResponse)
        Assertions.assertEquals((firstServerResponse as String), "PONG")

        clientConn.sendServerMessage(pingMessage)
        val secondServerResponse = clientConn.awaitServerMessage()

        Assertions.assertNotNull(secondServerResponse)
        Assertions.assertEquals((secondServerResponse as String), "PONG")
    }

    @Test
    fun shouldRespondPongToPing() {
        val clientConn = getServerConnection()
        val pingMessage = respEncoder.encode(RESPArray(listOf(BulkString("PING"))))

        clientConn.sendServerMessage(pingMessage)
        val serverResponse = clientConn.awaitServerMessage()

        Assertions.assertNotNull(serverResponse)
        Assertions.assertEquals((serverResponse as String), "PONG")
    }

    @Test
    fun shouldRespondToMultiplePings() {
        val firstClientConn = getServerConnection()
        val secondClientConn = getServerConnection()

        val pingMessage = respEncoder.encode(RESPArray(listOf(BulkString("PING"))))

        firstClientConn.sendServerMessage(pingMessage)
        secondClientConn.sendServerMessage(pingMessage)
        val firstServerResponse = firstClientConn.awaitServerMessage()
        val secondServerResponse = secondClientConn.awaitServerMessage()

        Assertions.assertNotNull(firstServerResponse)
        Assertions.assertNotNull(secondServerResponse)
        Assertions.assertEquals((firstServerResponse as String), "PONG")
        Assertions.assertEquals((secondServerResponse as String), "PONG")
    }
}