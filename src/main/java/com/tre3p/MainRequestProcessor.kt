package com.tre3p

import com.tre3p.handler.HandlerRouter
import com.tre3p.resp.RESPDecoder
import com.tre3p.resp.RESPEncoder
import org.apache.logging.log4j.kotlin.Logging
import java.io.InputStream

class MainRequestProcessor(
    private val respDecoder: RESPDecoder,
    private val respEncoder: RESPEncoder,
    private val router: HandlerRouter,
) : Logging {
    /**
     * processRequest() should be executed in a loop since one connection can send multiple commands to server
     */
    tailrec fun processRequest(
        requestInputStream: InputStream,
        // requestOutputStream: OutputStream,
    ): ByteArray {
        val decodedStatement = respDecoder.decode(requestInputStream) ?: return ByteArray(0)
        logger.info("Got instruction: $decodedStatement")

        val response = router.route(decodedStatement as List<*>)
        return respEncoder.encode(response)

        /*        router.route(decodedStatement as List<*>).let {
                    logger.info("Encoding and sending response: $it")
                    val encodedResponse = respEncoder.encode(it)
                    requestOutputStream.write(encodedResponse)
                }

                processRequest(requestInputStream, requestOutputStream)*/
    }
}
