package com.tre3p.unit.resp

import com.tre3p.resp.RESPDecoder
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RESPDecoderTest {
    private val respDecoder: RESPDecoder = RESPDecoder()

    // RESP BulkString Tests
    @Test
    fun shouldCorrectlyParseBulkString() {
        val t = "\$5\r\nHello\r\n".byteInputStream()
        val parseResult = respDecoder.decode(t) as String

        assertEquals(parseResult, "Hello")
    }

    @Test
    fun shouldCorrectlyParseEmptyBulkString() {
        val t = "\$0\r\n\r\n".byteInputStream()
        val parseResult = respDecoder.decode(t) as String

        assertEquals(parseResult, "")
    }

    @Test
    fun shouldCorrectlyParseBulkStringWithNegativeLength() {
        val t = "\$-15\r\n\r\n".byteInputStream()
        val parseResult = respDecoder.decode(t) as String

        assertEquals(parseResult, "")
    }

    // RESP Array Tests
    @Test
    fun shouldCorrectlyParseArrayOfBulkStrings() {
        val t = "*2\r\n\$4\r\nECHO\r\n\$3\r\nhey\r\n".byteInputStream()
        val parseResult = respDecoder.decode(t) as List<*>

        assertEquals(2, parseResult.size)
        assertEquals("ECHO", parseResult[0])
        assertEquals("hey", parseResult[1])
    }

    @Test
    fun shouldCorrectlyParseEmptyArray() {
        val t = "*0\r\n".byteInputStream()
        val parseResult = respDecoder.decode(t) as List<*>

        assertEquals(0, parseResult.size)
    }

    @Test
    fun shouldCorrectlyReactToNegativeLength() {
        val t = "*-5\r\n".byteInputStream()
        val parseResult = respDecoder.decode(t) as List<*>

        assertEquals(0, parseResult.size)
    }
}
