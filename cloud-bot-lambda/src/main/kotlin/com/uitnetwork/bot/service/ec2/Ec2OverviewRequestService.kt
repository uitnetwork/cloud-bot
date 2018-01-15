package com.uitnetwork.bot.service.ec2

import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.model.FulfillmentResponse
import com.uitnetwork.bot.service.AbstractRequestService
import com.uitnetwork.bot.service.PermissionService
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class Ec2OverviewRequestService(private val ec2Service: Ec2Service, private val permissionService: PermissionService) : AbstractRequestService(permissionService) {
    companion object {
        private val logger = KotlinLogging.logger { }

        const val ACTION_EC2_OVERVIEW = "EC2_OVERVIEW"
    }

    override fun getProcessableActionName(): String {
        return ACTION_EC2_OVERVIEW
    }

    override fun doProcess(fulfillmentRequest: FulfillmentRequest): FulfillmentResponse {
        return FulfillmentResponse(ec2Service.getEc2Overview())
    }
}
