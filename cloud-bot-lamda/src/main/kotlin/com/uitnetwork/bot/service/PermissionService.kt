package com.uitnetwork.bot.service

import com.uitnetwork.bot.model.Source
import com.uitnetwork.bot.model.Source.SLACK
import org.springframework.stereotype.Service

@Service
class PermissionService {
    companion object {
        const val ACTION_EC2_OVERVIEW = "EC2_OVERVIEW"
        const val HARDCODE_SLACK_USER = "U0T96K5Q9"
    }

    fun hasPermissionToExecute(userId: String, source: Source, action: String): Boolean {
        if (action == ACTION_EC2_OVERVIEW) {
            return true
        }

        if (userId == HARDCODE_SLACK_USER && source == SLACK) {
            return true
        }

        return false

    }
}
