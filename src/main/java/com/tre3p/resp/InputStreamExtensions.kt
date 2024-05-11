package com.tre3p.resp

import java.io.InputStream
import java.lang.Exception
import java.lang.StringBuilder

fun InputStream.readCrLfTerminatedInt(): Int {
    val sb = StringBuilder()

    var byteRead: Int
    while (true) {
        byteRead = this.read()
        if (byteRead == -1) throw Exception("End of stream reached unexpectedly")

        if (byteRead.toChar() == '\r') {
            val nextByte = this.read()
            if (nextByte == -1) throw Exception("End of stream reached unexpectedly")

            if (nextByte.toChar() == '\n') {
                break
            }
        }

        sb.append(byteRead.toChar())
    }

    return sb.toString().toInt()
}