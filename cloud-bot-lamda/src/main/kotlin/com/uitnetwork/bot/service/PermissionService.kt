package com.uitnetwork.bot.service

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.uitnetwork.bot.exception.CloudBotPermissionException
import com.uitnetwork.bot.model.CloudBotPermission
import com.uitnetwork.bot.model.Source
import com.uitnetwork.bot.service.ec2.Ec2OverviewRequestService.Companion.ACTION_EC2_OVERVIEW
import com.uitnetwork.bot.service.gce.GceOverviewRequestService.Companion.ACTION_GCE_OVERVIEW
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class PermissionService(private val dynamoDBMapper: DynamoDBMapper) {
    companion object {
        private val logger = KotlinLogging.logger { }
        private const val OVERVIEW = "OVERVIEW"
        private const val START = "START"
        private const val STOP = "STOP"
    }

    fun hasPermissionToExecute(userId: String, source: Source, action: String): Boolean {
        if (action == ACTION_EC2_OVERVIEW || action == ACTION_GCE_OVERVIEW) {
            return true
        }

        val dynamoDBQueryExpression = DynamoDBQueryExpression<CloudBotPermission>()
                .withKeyConditionExpression("source = :source AND userId = :userId")
                .withExpressionAttributeValues(mapOf(
                        ":source" to AttributeValue().withS(source.name),
                        ":userId" to AttributeValue().withS(userId)
                ))

        val cloudBotPermissions = dynamoDBMapper.query(CloudBotPermission::class.java, dynamoDBQueryExpression)

        if (cloudBotPermissions.size == 0) {
            return false
        }

        val requiredPermission = mapActionToRequiredPermission(action)
        val cloudBotPermission = cloudBotPermissions[0]

        logger.info { "CloudBotPermission: $cloudBotPermission" }

        return cloudBotPermission.permissions.contains(requiredPermission)
    }

    private fun mapActionToRequiredPermission(action: String): String {
        return when {
            action.contains(OVERVIEW) -> OVERVIEW
            action.contains(START) -> START
            action.contains(STOP) -> STOP
            else -> throw CloudBotPermissionException("Can not map action: $action to any permission")
        }
    }
}
