package com.tre3p.resp

import java.io.InputStream

open class RESPDecoder {
    fun decode(inputStream: InputStream): Any? =
        when (val readByte = inputStream.read().toByte()) {
            EOF_BYTE -> null
            ASTERISK_BYTE -> parseArray(inputStream)
            DOLLAR_BYTE -> parseString(inputStream)
            else -> throw Exception("$readByte byte type isn't supported yet")
        }

    private fun parseString(inputStream: InputStream): String {
        val stringLength = inputStream.readCrLfTerminatedInt()
        if (stringLength < 0) return ""

        val readString = String(inputStream.readNBytes(stringLength))

        inputStream.read()
        inputStream.read()

        return readString
    }

    private fun parseArray(inputStream: InputStream): List<Any> {
        val elementsCount = inputStream.readCrLfTerminatedInt()
        if (elementsCount < 0) return emptyList()

        val elements = mutableListOf<Any>()

        for (i in 0 until elementsCount) {
            elements.add(decode(inputStream)!!)
        }

        return elements
    }
}
