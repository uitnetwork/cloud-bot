package com.uitnetwork.bot.service.gce

import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.model.FulfillmentResponse
import com.uitnetwork.bot.service.AbstractRequestService
import com.uitnetwork.bot.service.PermissionService
import com.uitnetwork.bot.service.gce.GceStartRequestService.Companion.PARAM_GCE_NAME
import mu.KotlinLogging
import org.springframework.stereotype.Service


@Service
class GceStopRequestService(private val gceService: GceService,
                            private val permissionService: PermissionService) : AbstractRequestService(permissionService) {
    companion object {
        private val logger = KotlinLogging.logger { }

        const val ACTION_GCE_STOP = "GCE_STOP"
    }

    override fun getProcessableActionName(): String {
        return ACTION_GCE_STOP
    }

    override fun doProcess(fulfillmentRequest: FulfillmentRequest): FulfillmentResponse {
        val gceName = fulfillmentRequest.params[PARAM_GCE_NAME]
        logger.info { "Stopping $gceName" }
        if (gceName == null) {
            return FulfillmentResponse("Please specify the name of the GCE instance.")
        }

        gceService.stopGceInstance(gceName)

        return FulfillmentResponse("Stopping GCE: $gceName")
    }
}
