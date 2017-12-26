package com.uitnetwork.bot.handler

import com.amazonaws.services.lambda.runtime.Context
import com.uitnetwork.bot.config.CloutBotConfig
import com.uitnetwork.bot.model.ApiGatewayResponse
import com.uitnetwork.bot.model.ApiGatewayResponse.Companion.fromFulfillmentResponse
import com.uitnetwork.bot.model.FulfillmentResponse
import com.uitnetwork.bot.service.Ec2Service
import com.uitnetwork.bot.service.FulfillmentRequestService
import com.uitnetwork.bot.service.PermissionService
import org.apache.logging.log4j.LogManager

class Ec2StopRequestHandler : AbstractSpringAwareHandler<CloutBotConfig>() {
    companion object {
        private val logger = LogManager.getLogger(Ec2StopRequestHandler::class.java)
    }

    override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {
        logger.info("Received: ${input["body"]}")

        val fulfillmentRequestService = applicationContext.getBean(FulfillmentRequestService::class.java)
        val permissionService = applicationContext.getBean(PermissionService::class.java)

        val fulfillmentRequest = fulfillmentRequestService.parse(input["body"] as String)
        if (!permissionService.hasPermissionToExecute(fulfillmentRequest.userId, fulfillmentRequest.source, fulfillmentRequest.action)) {
            logger.info("Sorry. You don't have permission.")
            return fromFulfillmentResponse(FulfillmentResponse("Sorry. You don't have permission."))
//            return ApiGatewayResponse(body = "{\"speech\":\"This is a sample response from your webhook!\",\"displayText\":\"This is a sample response from your webhook!\"}")
        }

        logger.info("Stopping...")

        val name = "test123"

        val ec2Service = applicationContext.getBean(Ec2Service::class.java)
        val ec2InfoByInstanceName = ec2Service.getEc2InfoByInstanceName(name)

        if (ec2InfoByInstanceName.isEmpty()) {
            return fromFulfillmentResponse(FulfillmentResponse("There is no Ec2 instances which has name: $name"))
        }
        val ec2Ids = ec2InfoByInstanceName.map { it.id }
        ec2Service.stopEc2Instance(*ec2Ids.toTypedArray())

        return fromFulfillmentResponse(FulfillmentResponse("Stopping $name"))
    }
}
