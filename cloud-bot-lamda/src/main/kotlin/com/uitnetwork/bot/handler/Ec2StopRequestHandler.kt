package com.uitnetwork.bot.handler

import com.amazonaws.services.lambda.runtime.Context
import com.uitnetwork.bot.config.CloutBotConfig
import com.uitnetwork.bot.model.ApiGatewayResponse
import com.uitnetwork.bot.service.Ec2Service
import org.apache.logging.log4j.LogManager

class Ec2StopRequestHandler : AbstractSpringAwareHandler<CloutBotConfig>() {
    companion object {
        private val logger = LogManager.getLogger(Ec2StopRequestHandler::class.java)
    }

    override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {
        logger.info("Received: ${input.keys.toString()}")

        val name = "test123"

        val ec2Service = applicationContext.getBean(Ec2Service::class.java)
        val ec2InfoByInstanceName = ec2Service.getEc2InfoByInstanceName(name)

        if (ec2InfoByInstanceName.isEmpty()) {
            return ApiGatewayResponse(body = "There is no Ec2 instances which has name: $name")
        }
        val ec2Ids = ec2InfoByInstanceName.map { it.id }
        ec2Service.stopEc2Instance(*ec2Ids.toTypedArray())

        return ApiGatewayResponse(body = "Success stopping $ec2Ids")
    }
}
