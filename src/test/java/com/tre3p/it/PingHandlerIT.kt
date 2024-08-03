package com.tre3p.it

import com.tre3p.BaseIntegrationTest
import com.tre3p.resp.types.BulkString
import com.tre3p.resp.types.RESPArray
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

class PingHandlerIT : BaseIntegrationTest() {

    @Test
    fun shouldRespondPongToPing() {
        val clientConn = getServerConnection()
        val pingMessage = respEncoder.encode(RESPArray(listOf(BulkString("PING"))))

        sendServerMessage(clientConn, pingMessage)
        val serverResponse = awaitServerMessage(clientConn)
        val decodedServerResponse = respDecoder.decode(ByteArrayInputStream(serverResponse)) as String

        Assertions.assertEquals(decodedServerResponse, "PONG")
    }
}