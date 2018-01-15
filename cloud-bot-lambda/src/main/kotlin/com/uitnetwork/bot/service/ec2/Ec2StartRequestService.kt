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
        const val PARAM_EC2_NAME = "ec2Name"
    }

    override fun getProcessableActionName(): String {
        return ACTION_EC2_START
    }

    override fun doProcess(fulfillmentRequest: FulfillmentRequest): FulfillmentResponse {
        val ec2Id = fulfillmentRequest.params[PARAM_EC2_ID]
        val ec2Name = fulfillmentRequest.params[PARAM_EC2_NAME]

        logger.info { "Starting ec2Id: $ec2Id or ec2Name: $ec2Name" }

        return when {
            ec2Id != null && ec2Id.isNotBlank() -> startEc2InstanceById(ec2Id)
            ec2Name != null && ec2Name.isNotBlank() -> startEc2InstanceByName(ec2Name)
            else -> FulfillmentResponse("Please specify the name or id of the EC2 instance")
        }
    }

    private fun startEc2InstanceById(ec2Id: String): FulfillmentResponse {
        ec2Service.startEc2Instance(ec2Id)
        return FulfillmentResponse("Starting EC2: $ec2Id")
    }

    private fun startEc2InstanceByName(ec2Name: String): FulfillmentResponse {
        val ec2InstanceByInstanceName = ec2Service.getEc2InstancesByInstanceName(ec2Name)
        if (ec2InstanceByInstanceName.isEmpty()) {
            return FulfillmentResponse("There is no EC2 instances with name: $ec2Name")
        }

        val ec2Ids = ec2InstanceByInstanceName.map { it.id }.toTypedArray()
        ec2Service.startEc2Instance(*ec2Ids)
        return FulfillmentResponse("Starting EC2: ${ec2Ids.joinToString(separator = ", ")}")
    }
}
