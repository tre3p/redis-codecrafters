package com.tre3p

import com.tre3p.handler.HandlerRouter
import com.tre3p.resp.RESPDecoder
import com.tre3p.resp.RESPEncoder
import java.io.InputStream
import java.io.OutputStream

class MainRequestProcessor(
    private val respDecoder: RESPDecoder,
    private val respEncoder: RESPEncoder,
    private val router: HandlerRouter
) {
    fun processRequest(requestInputStream: InputStream, requestOutputStream: OutputStream) {
        val decodedRequest = respDecoder.decode(requestInputStream) as List<*>
        val response = router.route(decodedRequest)

        response?.let {
            val encodedResponse = respEncoder.encode(it)
            requestOutputStream.write(encodedResponse)
            requestOutputStream.close()
        }
    }
}