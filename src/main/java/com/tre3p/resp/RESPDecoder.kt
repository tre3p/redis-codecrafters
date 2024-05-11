package com.tre3p.resp

import java.io.InputStream
import java.lang.Exception

open class RESPDecoder {
    fun decode(inputStream: InputStream): Any? {
        val readByte = inputStream.read().toByte()

        return when (readByte) {
            EOF_BYTE -> null
            ASTERISK_BYTE -> parseArray(inputStream)
            DOLLAR_BYTE -> parseString(inputStream)
            else -> throw Exception("$readByte byte type isn't supported yet")
        }
    }

    private fun parseString(inputStream: InputStream): String {
        val stringLength = inputStream.readCrLfTerminatedInt()
        val readString = String(inputStream.readNBytes(stringLength))

        inputStream.read()
        inputStream.read()

        return readString
    }

    private fun parseArray(inputStream: InputStream): List<Any> {
        val elementsCount = inputStream.readCrLfTerminatedInt()
        val elements = mutableListOf<Any>()

        for (i in 0 until elementsCount) {
            elements.add(decode(inputStream)!!)
        }

        return elements
    }
}