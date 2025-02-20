package com.tre3p.integration

import com.tre3p.BaseIntegrationTest
import com.tre3p.resp.types.RESPArray
import com.tre3p.resp.types.SimpleString
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SetHandlerTest : BaseIntegrationTest() {
    @Test
    fun shouldReturnOKOnValidSyntax() {
        val clientConn = getServerConnection()
        val setMessage =
            respEncoder.encode(
                RESPArray(
                    listOf(
                        SimpleString("SET"),
                        SimpleString("test_key"),
                        SimpleString("test_value"),
                    ),
                ),
            )

        clientConn.sendServerMessage(setMessage)
        val serverResponse = clientConn.awaitServerMessage() as String

        Assertions.assertEquals(serverResponse, "OK")
    }

    @Test
    fun shouldReturnOKOnValidSyntaxWithPx() {
        val clientConn = getServerConnection()
        val setMessage =
            respEncoder.encode(
                RESPArray(
                    listOf(
                        SimpleString("SET"),
                        SimpleString("test_key"),
                        SimpleString("test_value"),
                        SimpleString("px"),
                        SimpleString("500"),
                    ),
                ),
            )

        clientConn.sendServerMessage(setMessage)
        val serverResponse = clientConn.awaitServerMessage() as String

        Assertions.assertEquals(serverResponse, "OK")
    }

    @Test
    fun shouldReturnErrorOnInvalidSyntax() {
        val clientConn = getServerConnection()
        val setMessage = respEncoder.encode(RESPArray(listOf(SimpleString("SET"), SimpleString("test_key"))))

        clientConn.sendServerMessage(setMessage)
        val serverResponse = clientConn.awaitServerMessage() as String

        Assertions.assertEquals(serverResponse, "Unexpected args count for SET command")
    }
}
