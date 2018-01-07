package com.uitnetwork.bot.service.ec2

import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.model.FulfillmentResponse
import com.uitnetwork.bot.service.AbstractRequestService
import com.uitnetwork.bot.service.PermissionService
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class Ec2StartRequestService(private val ec2Service: Ec2Service, private val permissionService: PermissionService) : AbstractRequestService(permissionService) {
    companion object {
        private val logger = KotlinLogging.logger { }

        const val ACTION_EC2_START = "EC2_START"
        const val PARAM_EC2_ID = "ec2Id"
    }

    override fun getProcessableActionName(): String {
        return ACTION_EC2_START
    }

    override fun doProcess(fulfillmentRequest: FulfillmentRequest): FulfillmentResponse {
        val ec2Id = fulfillmentRequest.params[PARAM_EC2_ID]
        logger.info { "Starting $ec2Id" }
        if (ec2Id == null) {
            return FulfillmentResponse("Please specify the id of the EC2 instance.")
        }

        ec2Service.startEc2Instance(ec2Id)

        return FulfillmentResponse("Starting EC2: $ec2Id")
    }
}
