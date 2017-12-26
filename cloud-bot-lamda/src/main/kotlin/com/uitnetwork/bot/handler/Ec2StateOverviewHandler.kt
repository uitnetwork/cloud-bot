package com.uitnetwork.bot.handler

import com.amazonaws.services.lambda.runtime.Context
import com.uitnetwork.bot.config.CloutBotConfig
import com.uitnetwork.bot.model.ApiGatewayResponse
import com.uitnetwork.bot.model.FulfillmentResponse
import com.uitnetwork.bot.service.Ec2Service
import org.apache.logging.log4j.LogManager

class Ec2StateOverviewHandler : AbstractSpringAwareHandler<CloutBotConfig>() {
    companion object {
        private val logger = LogManager.getLogger(Ec2StateOverviewHandler::class.java)
    }

    override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {
        logger.info("Received: ${input.keys.toString()}")

        val ec2Service = applicationContext.getBean(Ec2Service::class.java)
        val allEc2Info = ec2Service.getAllEc2Info()

        val response = FulfillmentResponse(allEc2Info.joinToString { it.toString() })
        return ApiGatewayResponse.fromFulfillmentResponse(response)
    }
}
