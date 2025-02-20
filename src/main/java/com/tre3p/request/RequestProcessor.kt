package com.tre3p.request

import com.tre3p.handler.HandlerRouter
import com.tre3p.resp.RESPDecoder
import com.tre3p.resp.RESPEncoder
import org.apache.logging.log4j.kotlin.Logging
import java.io.InputStream

class RequestProcessor(
    private val respDecoder: RESPDecoder,
    private val respEncoder: RESPEncoder,
    private val router: HandlerRouter,
) : Logging {
    fun processRequest(requestInputStream: InputStream): ByteArray {
        val decodedStatement = respDecoder.decode(requestInputStream)
        logger.info("Got instruction: $decodedStatement")

        val response = router.route(decodedStatement as? List<*>)
        return respEncoder.encode(response)
    }
}
