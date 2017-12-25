package com.uitnetwork.bot.model

import java.time.LocalDateTime

data class BotResponse(val message: String, val time: LocalDateTime) : Response()
