package com.tre3p.resp

import java.io.InputStream
import java.lang.Exception

open class RESPDecoder {
    fun decode(inputStream: InputStream): Any? {
        return when (val readByte = inputStream.read().toByte()) {
            EOF_BYTE -> null
            ASTERISK_BYTE -> parseArray(inputStream)
            DOLLAR_BYTE -> parseBulkString(inputStream)
            PLUS_BYTE -> parseSimpleString(inputStream)
            else -> throw Exception("$readByte byte type isn't supported yet")
        }
    }

    private fun parseSimpleString(inputStream: InputStream): String {
        return inputStream.readCrLfTerminatedElement()
    }

    private fun parseBulkString(inputStream: InputStream): String {
        val stringLength = inputStream.readCrLfTerminatedElement().toInt()
        if (stringLength < 0) return ""

        val readString = String(inputStream.readNBytes(stringLength))

        inputStream.read()
        inputStream.read()

        return readString
    }

    private fun parseArray(inputStream: InputStream): List<Any> {
        val elementsCount = inputStream.readCrLfTerminatedElement().toInt()
        if (elementsCount < 0) return emptyList()

        val elements = mutableListOf<Any>()

        for (i in 0 until elementsCount) {
            elements.add(decode(inputStream)!!)
        }

        return elements
    }
}