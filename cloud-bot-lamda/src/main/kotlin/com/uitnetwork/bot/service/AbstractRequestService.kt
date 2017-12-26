package com.uitnetwork.bot.service

import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.model.FulfillmentResponse

abstract class AbstractRequestService {

    abstract fun getProcessableAction(): String

    abstract fun process(fulfillmentRequest: FulfillmentRequest): FulfillmentResponse
}
