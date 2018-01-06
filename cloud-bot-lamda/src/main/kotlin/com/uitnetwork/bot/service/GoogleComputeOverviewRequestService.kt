package com.uitnetwork.bot.service

import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.model.FulfillmentResponse
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class GoogleComputeOverviewRequestService(val googleComputeService: GoogleComputeService) : AbstractRequestService() {
    companion object {
        private val logger = KotlinLogging.logger { }

        const val ACTION_GOOGLE_COMPUTE_OVERVIEW = "GOOGLE_COMPUTE_OVERVIEW"
    }

    override fun getProcessableAction(): String {
        return ACTION_GOOGLE_COMPUTE_OVERVIEW
    }

    override fun process(fulfillmentRequest: FulfillmentRequest): FulfillmentResponse {
        return FulfillmentResponse(googleComputeService.getComputeOverview())
    }
}
