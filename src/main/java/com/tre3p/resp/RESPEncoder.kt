package com.tre3p.resp

import com.tre3p.resp.types.BulkString
import com.tre3p.resp.types.SimpleString
import java.lang.Exception

class RESPEncoder {
    fun encode(args: Any): ByteArray {
        return when(args) {
            is BulkString -> encodeBulkString(args)
            is SimpleString -> encodeSimpleString(args)
            else -> throw Exception("Unknown RESP data type provided")
        }
    }

    private fun encodeBulkString(bulkString: BulkString): ByteArray {
        return byteArrayOf(DOLLAR_BYTE)
            .plus(bulkString.length.toString().encodeToByteArray())
            .plus(CR).plus(LF)
            .plus(bulkString.data.encodeToByteArray())
            .plus(CR).plus(LF)
    }

    private fun encodeSimpleString(simpleString: SimpleString): ByteArray {
        return byteArrayOf(PLUS_BYTE)
            .plus(simpleString.data.encodeToByteArray())
            .plus(CR).plus(LF)
    }
}