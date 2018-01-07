package com.uitnetwork.bot.service

import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.model.FulfillmentResponse
import org.springframework.stereotype.Service

@Service
class RequestServiceManager(private val requestServices: List<AbstractRequestService>) {
    private val requestServiceMap: Map<String, AbstractRequestService> = requestServices.map { it.getProcessableActionName() to it }.toMap()

    fun process(fulfillmentRequest: FulfillmentRequest): FulfillmentResponse {
        val requestService = requestServiceMap[fulfillmentRequest.action.toUpperCase()]
                ?: return FulfillmentResponse("Sorry. I can not handle the request with action ${fulfillmentRequest.action}")

        return requestService.validatePermissionThenProcess(fulfillmentRequest)
    }
}
