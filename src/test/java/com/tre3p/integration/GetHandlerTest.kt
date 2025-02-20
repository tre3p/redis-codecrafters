package com.tre3p.integration

import com.tre3p.BaseIntegrationTest
import com.tre3p.resp.types.RESPArray
import com.tre3p.resp.types.SimpleString
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GetHandlerTest : BaseIntegrationTest() {
    @Test
    fun shouldReturnSetValue() {
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
        val getMessage = respEncoder.encode(RESPArray(listOf(SimpleString("GET"), SimpleString("test_key"))))

        clientConn.sendServerMessage(setMessage)
        val okResponse = clientConn.awaitServerMessage() as String

        clientConn.sendServerMessage(getMessage)
        val valueResponse = clientConn.awaitServerMessage() as String

        Assertions.assertEquals(okResponse, "OK")
        Assertions.assertEquals(valueResponse, "test_value")
    }

    @Test
    fun shouldCorrectlyHandleExpiredValue() {
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
        val getMessage = respEncoder.encode(RESPArray(listOf(SimpleString("GET"), SimpleString("test_key"))))

        clientConn.sendServerMessage(setMessage)
        val okResponse = clientConn.awaitServerMessage() as String

        clientConn.sendServerMessage(getMessage)
        val valueResponse = clientConn.awaitServerMessage() as String

        Assertions.assertEquals(okResponse, "OK")
        Assertions.assertEquals(valueResponse, "test_value")

        Thread.sleep(500)

        clientConn.sendServerMessage(getMessage)
        val expiredValueResponse = clientConn.awaitServerMessage() as String
        Assertions.assertEquals("", expiredValueResponse)
    }
}
