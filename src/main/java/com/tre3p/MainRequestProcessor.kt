package com.tre3p

import com.tre3p.handler.HandlerRouter
import com.tre3p.resp.RESPDecoder
import com.tre3p.resp.RESPEncoder
import org.apache.logging.log4j.kotlin.Logging
import java.io.InputStream
import java.io.OutputStream

class MainRequestProcessor(
    private val respDecoder: RESPDecoder,
    private val respEncoder: RESPEncoder,
    private val router: HandlerRouter
): Logging {
    tailrec fun processRequest(requestInputStream: InputStream, requestOutputStream: OutputStream) {
        val decodedStatement = respDecoder.decode(requestInputStream) ?: return
        logger.info("Got instruction: $decodedStatement")

        router.route(decodedStatement as List<*>).let {
            logger.info("Encoding and sending response: $it")
            val encodedResponse = respEncoder.encode(it)
            requestOutputStream.write(encodedResponse)
        }

        processRequest(requestInputStream, requestOutputStream)
    }
}