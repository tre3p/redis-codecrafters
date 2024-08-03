package com.tre3p.it

import com.tre3p.BaseIntegrationTest
import com.tre3p.resp.types.RESPArray
import com.tre3p.resp.types.SimpleString
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EchoHandlerTest : BaseIntegrationTest() {

    @Test
    fun shouldCorrectlyHandleEchoCommand() {
        val clientConn = getServerConnection()
        val echoMessage = respEncoder.encode(RESPArray(listOf(SimpleString("ECHO"), SimpleString("test_message"))))

        clientConn.sendServerMessage(echoMessage)
        val serverResponse = clientConn.awaitServerMessage() as String

        Assertions.assertEquals(serverResponse, "test_message")
    }
}