package com.uitnetwork.bot.service

import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.model.FulfillmentResponse
import mu.KotlinLogging
import org.springframework.stereotype.Service


@Service
class Ec2StopRequestService(val ec2Service: Ec2Service, val permissionService: PermissionService) : AbstractRequestService() {
    companion object {
        private val logger = KotlinLogging.logger { }

        const val ACTION_EC2_STOP = "EC2_STOP"
        const val PARAM_EC2_ID = "ec2Id"
    }

    override fun getProcessableAction(): String {
        return ACTION_EC2_STOP
    }

    override fun process(fulfillmentRequest: FulfillmentRequest): FulfillmentResponse {

        if (!permissionService.hasPermissionToExecute(fulfillmentRequest.userId, fulfillmentRequest.source, fulfillmentRequest.action)) {
            logger.info { "Sorry. You don't have permission." }
            return FulfillmentResponse("Sorry. You don't have permission.")
        }

        val ec2Id = fulfillmentRequest.params[PARAM_EC2_ID]
        logger.info { "Stopping $ec2Id" }
        if (ec2Id == null) {
            return FulfillmentResponse("Please specify the id of the instance.")
        }

//        val name = "test123"
//
//        val ec2InfoByInstanceName = ec2Service.getEc2InfoByInstanceName(name)
//
//        if (ec2InfoByInstanceName.isEmpty()) {
//            return FulfillmentResponse("There is no Ec2 instances which has name: $name")
//        }
//        val ec2Ids = ec2InfoByInstanceName.map { it.id }
//        ec2Service.stopEc2Instance(*ec2Ids.toTypedArray())
        ec2Service.stopEc2Instance(ec2Id)

        return FulfillmentResponse("Stopping $ec2Id")
    }
}
