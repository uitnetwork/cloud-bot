package com.uitnetwork.bot.service

import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.model.FulfillmentResponse
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service


@Service
class Ec2StopRequestService(val ec2Service: Ec2Service, val permissionService: PermissionService) : AbstractRequestService() {
    companion object {
        private val logger = LogManager.getLogger(Ec2StopRequestService::class.java)

        const val ACTION_EC2_STOP = "EC2_STOP"
    }

    override fun getProcessableAction(): String {
        return ACTION_EC2_STOP
    }

    override fun process(fulfillmentRequest: FulfillmentRequest): FulfillmentResponse {

        if (!permissionService.hasPermissionToExecute(fulfillmentRequest.userId, fulfillmentRequest.source, fulfillmentRequest.action)) {
            logger.info("Sorry. You don't have permission.")
            return FulfillmentResponse("Sorry. You don't have permission.")
        }

        logger.info("Stopping...")

        val name = "test123"

        val ec2InfoByInstanceName = ec2Service.getEc2InfoByInstanceName(name)

        if (ec2InfoByInstanceName.isEmpty()) {
            return FulfillmentResponse("There is no Ec2 instances which has name: $name")
        }
        val ec2Ids = ec2InfoByInstanceName.map { it.id }
        ec2Service.stopEc2Instance(*ec2Ids.toTypedArray())

        return FulfillmentResponse("Stopping $name")
    }
}
