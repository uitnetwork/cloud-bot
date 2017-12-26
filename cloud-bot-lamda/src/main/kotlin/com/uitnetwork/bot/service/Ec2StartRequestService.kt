package com.uitnetwork.bot.service

import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.model.FulfillmentResponse
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service

@Service
class Ec2StartRequestService(val ec2Service: Ec2Service) : AbstractRequestService() {
    companion object {
        private val logger = LogManager.getLogger(Ec2StartRequestService::class.java)

        const val ACTION_EC2_START = "EC2_START"
    }

    override fun getProcessableAction(): String {
        return ACTION_EC2_START
    }

    override fun process(fulfillmentRequest: FulfillmentRequest): FulfillmentResponse {
        val id = "i-050fd2b4cd3f386b2"

        ec2Service.startEc2Instance(id)

        return FulfillmentResponse("Starting $id")
    }
}
