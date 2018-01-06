package com.uitnetwork.bot.service

import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.model.FulfillmentResponse
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class Ec2OverviewRequestService(val ec2Service: Ec2Service) : AbstractRequestService() {
    companion object {
        private val logger = KotlinLogging.logger { }

        const val ACTION_EC2_OVERVIEW = "EC2_OVERVIEW"
    }

    override fun getProcessableAction(): String {
        return ACTION_EC2_OVERVIEW
    }

    override fun process(fulfillmentRequest: FulfillmentRequest): FulfillmentResponse {
        return FulfillmentResponse(ec2Service.getEc2Overview())
    }
}
