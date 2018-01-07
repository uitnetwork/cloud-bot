package com.uitnetwork.bot.service

import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.model.FulfillmentResponse
import mu.KotlinLogging

abstract class AbstractRequestService(private val permissionService: PermissionService) {
    companion object {
        private val logger = KotlinLogging.logger { }
    }

    abstract fun getProcessableActionName(): String

    fun validatePermissionThenProcess(fulfillmentRequest: FulfillmentRequest): FulfillmentResponse {
        if (!permissionService.hasPermissionToExecute(fulfillmentRequest.userId, fulfillmentRequest.source, fulfillmentRequest.action)) {
            return FulfillmentResponse("Sorry. You don't have permission")
        }

        return doProcess(fulfillmentRequest)
    }

    abstract fun doProcess(fulfillmentRequest: FulfillmentRequest): FulfillmentResponse
}
