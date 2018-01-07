package com.uitnetwork.bot.service.ec2

import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.model.FulfillmentResponse
import com.uitnetwork.bot.service.AbstractRequestService
import com.uitnetwork.bot.service.PermissionService
import com.uitnetwork.bot.service.ec2.Ec2StartRequestService.Companion.PARAM_EC2_ID
import mu.KotlinLogging
import org.springframework.stereotype.Service


@Service
class Ec2StopRequestService(private val ec2Service: Ec2Service, private val permissionService: PermissionService) : AbstractRequestService(permissionService) {
    companion object {
        private val logger = KotlinLogging.logger { }

        const val ACTION_EC2_STOP = "EC2_STOP"
    }

    override fun getProcessableActionName(): String {
        return ACTION_EC2_STOP
    }

    override fun doProcess(fulfillmentRequest: FulfillmentRequest): FulfillmentResponse {

        val ec2Id = fulfillmentRequest.params[PARAM_EC2_ID]
        logger.info { "Stopping $ec2Id" }
        if (ec2Id == null) {
            return FulfillmentResponse("Please specify the id of the EC2 instance.")
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

        return FulfillmentResponse("Stopping EC2: $ec2Id")
    }
}
