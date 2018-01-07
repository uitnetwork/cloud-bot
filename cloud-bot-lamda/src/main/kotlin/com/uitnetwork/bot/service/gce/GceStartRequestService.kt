package com.uitnetwork.bot.service.gce

import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.model.FulfillmentResponse
import com.uitnetwork.bot.service.AbstractRequestService
import com.uitnetwork.bot.service.PermissionService
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class GceStartRequestService(private val gceService: GceService,
                             private val permissionService: PermissionService) : AbstractRequestService(permissionService) {
    companion object {
        private val logger = KotlinLogging.logger { }

        const val ACTION_GCE_START = "GCE_START"
        const val PARAM_GCE_NAME = "gceName"
    }

    override fun getProcessableActionName(): String {
        return ACTION_GCE_START
    }

    override fun doProcess(fulfillmentRequest: FulfillmentRequest): FulfillmentResponse {
        val gceName = fulfillmentRequest.params[PARAM_GCE_NAME]
        logger.info { "Starting $gceName" }
        if (gceName == null) {
            return FulfillmentResponse("Please specify the name of the GCE instance.")
        }

        gceService.startGceInstance(gceName)

        return FulfillmentResponse("Starting GCE: $gceName")
    }
}
