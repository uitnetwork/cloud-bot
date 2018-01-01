package com.uitnetwork.bot.service

import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.model.FulfillmentResponse
import com.uitnetwork.bot.service.Ec2StopRequestService.Companion.PARAM_EC2_ID
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class Ec2StartRequestService(private val ec2Service: Ec2Service, private val permissionService: PermissionService) : AbstractRequestService() {
    companion object {
        private val logger = KotlinLogging.logger { }

        const val ACTION_EC2_START = "EC2_START"
    }

    override fun getProcessableAction(): String {
        return ACTION_EC2_START
    }

    override fun process(fulfillmentRequest: FulfillmentRequest): FulfillmentResponse {
        if (!permissionService.hasPermissionToExecute(fulfillmentRequest.userId, fulfillmentRequest.source, fulfillmentRequest.action)) {
            logger.info { "Sorry. You don't have permission." }
            return FulfillmentResponse("Sorry. You don't have permission.")
        }

        val ec2Id = fulfillmentRequest.params[PARAM_EC2_ID]
        logger.info { "Starting $ec2Id" }
        if (ec2Id == null) {
            return FulfillmentResponse("Please specify the id of the instance.")
        }

        ec2Service.startEc2Instance(ec2Id)

        return FulfillmentResponse("Starting $ec2Id")
    }
}
