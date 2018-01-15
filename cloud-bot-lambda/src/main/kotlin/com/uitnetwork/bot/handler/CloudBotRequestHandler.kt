package com.uitnetwork.bot.handler

import com.amazonaws.services.lambda.runtime.Context
import com.uitnetwork.bot.config.CloutBotConfig
import com.uitnetwork.bot.model.ApiGatewayResponse
import com.uitnetwork.bot.model.ApiGatewayResponse.Companion.fromFulfillmentResponse
import com.uitnetwork.bot.model.FulfillmentResponse
import com.uitnetwork.bot.service.FulfillmentRequestParser
import com.uitnetwork.bot.service.RequestServiceManager
import mu.KotlinLogging
import java.util.*

class CloudBotRequestHandler : AbstractSpringAwareHandler<CloutBotConfig>() {
    companion object {
        private val logger = KotlinLogging.logger { }
    }

    override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {
        try {
            logger.debug { "Received: ${input["body"]}" }

            val fulfillmentRequestService = applicationContext.getBean(FulfillmentRequestParser::class.java)
            val fulfillmentRequest = fulfillmentRequestService.parse(input["body"] as String)

            val requestServiceManager = applicationContext.getBean(RequestServiceManager::class.java)
            val fulfillmentResponse = requestServiceManager.process(fulfillmentRequest)

            return fromFulfillmentResponse(fulfillmentResponse)
        } catch (exception: Exception) {
            val randomUuidAsErrorId = UUID.randomUUID().toString()

            logger.error(exception) { "Error id: $randomUuidAsErrorId" }
            return fromFulfillmentResponse(FulfillmentResponse("There is an error while processing the request. Error id: $randomUuidAsErrorId"))
        }
    }
}
