package com.uitnetwork.botadmin.model

import com.amazonaws.services.dynamodbv2.datamodeling.*
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id

@DynamoDBTable(tableName = "CloudBotPermission")
data class CloudBotPermission(
        @DynamoDBHashKey
        var userId: String,

        @DynamoDBRangeKey
        var source: String,

        @DynamoDBAttribute
        var permissions: List<String>,

        @Id
        @DynamoDBIgnore
        @JsonIgnore
        var id: CloudBotPermissionId = CloudBotPermissionId(userId, source)
)

data class CloudBotPermissionId(
        @DynamoDBHashKey
        var userId: String,

        @DynamoDBRangeKey
        var source: String
)


