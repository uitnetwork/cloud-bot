package com.uitnetwork.bot.service

import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.model.FulfillmentResponse
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service

@Service
class Ec2OverviewRequestService(val ec2Service: Ec2Service) : AbstractRequestService() {
    companion object {
        private val logger = LogManager.getLogger(Ec2OverviewRequestService::class.java)

        const val ACTION_EC2_OVERVIEW = "EC2_OVERVIEW"
    }

    override fun getProcessableAction(): String {
        return ACTION_EC2_OVERVIEW
    }

    override fun process(fulfillmentRequest: FulfillmentRequest): FulfillmentResponse {
        val allEc2Info = ec2Service.getAllEc2Info()

        val allEc2InfoAsString = FulfillmentResponse(allEc2Info.joinToString { it.toString() })

        return FulfillmentResponse("Ec2 Overview\n$allEc2InfoAsString")
    }
}
