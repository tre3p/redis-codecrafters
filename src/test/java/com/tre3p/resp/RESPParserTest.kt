package com.tre3p.resp

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RESPParserTest {

    private val respParser: RESPDecoder = RESPDecoder()

    @Test
    fun shouldCorrectlyParseArrayOfBulkStrings() {
        val t = "*2\r\n\$4\r\nECHO\r\n\$3\r\nhey\r\n".byteInputStream()
        val parseResult = respParser.decode(t) as List<*>

        assertEquals(2, parseResult.size)
        assertEquals("ECHO", parseResult[0])
        assertEquals("hey", parseResult[1])
    }
}