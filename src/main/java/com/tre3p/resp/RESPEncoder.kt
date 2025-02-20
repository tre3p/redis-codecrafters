package com.tre3p.resp

import com.tre3p.resp.types.BulkString
import com.tre3p.resp.types.RESPArray
import com.tre3p.resp.types.SimpleString

class RESPEncoder {
    fun encode(args: Any): ByteArray =
        when (args) {
            is BulkString -> encodeBulkString(args)
            is SimpleString -> encodeSimpleString(args)
            is RESPArray -> encodeRespArray(args)
            else -> throw Exception("Unknown RESP data type provided")
        }

    private fun encodeRespArray(respArray: RESPArray): ByteArray {
        var initialByteArray =
            byteArrayOf(ASTERISK_BYTE)
                .plus(
                    respArray.data.size
                        .toString()
                        .encodeToByteArray(),
                ).plus(CR)
                .plus(LF)

        respArray.data.forEach {
            initialByteArray =
                initialByteArray
                    .plus(encode(it))
        }

        return initialByteArray
    }

    private fun encodeBulkString(bulkString: BulkString): ByteArray {
        val encodedLength =
            byteArrayOf(DOLLAR_BYTE)
                .plus(bulkString.length.toString().encodeToByteArray())
                .plus(CR)
                .plus(LF)

        return if (bulkString.data.isNullOrBlank()) {
            encodedLength
        } else {
            encodedLength
                .plus(bulkString.data.encodeToByteArray())
                .plus(CR)
                .plus(LF)
        }
    }

    private fun encodeSimpleString(simpleString: SimpleString): ByteArray =
        byteArrayOf(PLUS_BYTE)
            .plus(simpleString.data.encodeToByteArray())
            .plus(CR)
            .plus(LF)
}
