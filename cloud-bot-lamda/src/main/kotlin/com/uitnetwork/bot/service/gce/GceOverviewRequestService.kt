package com.uitnetwork.bot.service.gce

import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.model.FulfillmentResponse
import com.uitnetwork.bot.service.AbstractRequestService
import com.uitnetwork.bot.service.PermissionService
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class GceOverviewRequestService(private val gceService: GceService,
                                private val permissionService: PermissionService) : AbstractRequestService(permissionService) {
    companion object {
        private val logger = KotlinLogging.logger { }

        const val ACTION_GCE_OVERVIEW = "GCE_OVERVIEW"
    }

    override fun getProcessableActionName(): String {
        return ACTION_GCE_OVERVIEW
    }

    override fun doProcess(fulfillmentRequest: FulfillmentRequest): FulfillmentResponse {
        return FulfillmentResponse(gceService.getGceOverview())
    }
}
