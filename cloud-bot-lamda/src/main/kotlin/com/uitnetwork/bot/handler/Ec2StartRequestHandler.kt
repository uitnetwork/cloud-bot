package com.uitnetwork.bot.handler

import com.amazonaws.services.lambda.runtime.Context
import com.uitnetwork.bot.config.CloutBotConfig
import com.uitnetwork.bot.model.ApiGatewayResponse
import com.uitnetwork.bot.service.Ec2Service
import org.apache.logging.log4j.LogManager

class Ec2StartRequestHandler : AbstractSpringAwareHandler<CloutBotConfig>() {
    companion object {
        private val logger = LogManager.getLogger(Ec2StartRequestHandler::class.java)
    }

    override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {
        logger.info("Received: ${input.keys.toString()}")

        val id = "i-050fd2b4cd3f386b2"

        val ec2Service = applicationContext.getBean(Ec2Service::class.java)
        ec2Service.startEc2Instance(id)

        return ApiGatewayResponse(body = "Success")
    }
}
