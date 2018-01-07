package com.uitnetwork.bot.service.ec2

import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.model.FulfillmentResponse
import com.uitnetwork.bot.service.AbstractRequestService
import com.uitnetwork.bot.service.PermissionService
import com.uitnetwork.bot.service.ec2.Ec2StartRequestService.Companion.PARAM_EC2_ID
import com.uitnetwork.bot.service.ec2.Ec2StartRequestService.Companion.PARAM_EC2_NAME
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
        val ec2Name = fulfillmentRequest.params[PARAM_EC2_NAME]

        logger.info { "Stopping ec2Id: $ec2Id or ec2Name: $ec2Name" }

        return when {
            ec2Id != null -> stopEc2InstanceById(ec2Id)
            ec2Name != null -> stopEc2InstanceByName(ec2Name)
            else -> FulfillmentResponse("Please specify the name or id of the EC2 instance.")
        }
    }

    private fun stopEc2InstanceById(ec2Id: String): FulfillmentResponse {
        ec2Service.stopEc2Instance(ec2Id)
        return FulfillmentResponse("Stopping EC2: $ec2Id")
    }

    private fun stopEc2InstanceByName(ec2Name: String): FulfillmentResponse {
        val ec2InstanceByInstanceName = ec2Service.getEc2InstancesByInstanceName(ec2Name)
        if (ec2InstanceByInstanceName.isEmpty()) {
            return FulfillmentResponse("There is no EC2 instances with name: $ec2Name")
        }

        val ec2Ids = ec2InstanceByInstanceName.map { it.id }.toTypedArray()
        ec2Service.stopEc2Instance(*ec2Ids)
        return FulfillmentResponse("Stopping EC2: $ec2Ids")
    }
}
