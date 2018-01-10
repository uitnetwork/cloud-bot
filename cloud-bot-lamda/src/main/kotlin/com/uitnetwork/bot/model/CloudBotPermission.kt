package com.uitnetwork.bot.model

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable

@DynamoDBTable(tableName = "CloudBotPermission")
data class CloudBotPermission(

        @DynamoDBHashKey
        var userId: String,

        @DynamoDBRangeKey
        var source: String,

        @DynamoDBAttribute
        var permissions: List<String>
)


