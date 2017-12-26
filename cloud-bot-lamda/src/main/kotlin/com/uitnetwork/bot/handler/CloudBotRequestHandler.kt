package com.uitnetwork.bot.handler

import com.amazonaws.services.lambda.runtime.Context
import com.uitnetwork.bot.config.CloutBotConfig
import com.uitnetwork.bot.model.ApiGatewayResponse
import com.uitnetwork.bot.model.ApiGatewayResponse.Companion.fromFulfillmentResponse
import com.uitnetwork.bot.service.FulfillmentRequestParser
import com.uitnetwork.bot.service.RequestServiceManager
import org.apache.logging.log4j.LogManager

class CloudBotRequestHandler : AbstractSpringAwareHandler<CloutBotConfig>() {
    companion object {
        private val logger = LogManager.getLogger(CloudBotRequestHandler::class.java)
    }

    override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {
        logger.info("Received: ${input["body"]}")

        val fulfillmentRequestService = applicationContext.getBean(FulfillmentRequestParser::class.java)
        val fulfillmentRequest = fulfillmentRequestService.parse(input["body"] as String)

        val requestServiceManager = applicationContext.getBean(RequestServiceManager::class.java)
        val fulfillmentResponse = requestServiceManager.process(fulfillmentRequest)

        return fromFulfillmentResponse(fulfillmentResponse)
    }
}
