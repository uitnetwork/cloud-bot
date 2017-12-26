package com.uitnetwork.bot.model

import java.time.ZonedDateTime

data class FulfillmentRequest(
        val source: Source,
        val userId: String,
        val userText: String,
        val action: String,
        val params: Map<String, String> = emptyMap(),
        val requestTime: ZonedDateTime,
        val requestId: String
)

enum class Source {
    SKYPE,
    SLACK
}
