package com.uitnetwork.bot.config

import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.get

@Configuration
class DynamoDBConfig(private val env: Environment) {
    companion object {
        const val AWS_DYNAMODB_ENDPOINT = "AWS_DYNAMODB_ENDPOINT"
        const val AWS_DYNAMODB_REGION = "AWS_DYNAMODB_REGION"
    }

    private val amazoneDynamoDbEndpoint: String by lazy {
        env[AWS_DYNAMODB_ENDPOINT]
    }

    private val amazoneDynamoDbRegion: String by lazy {
        env[AWS_DYNAMODB_REGION]
    }

    @Bean
    fun amazonDynamoDB(): AmazonDynamoDB {
        val amazonDynamoDBClientBuilder = AmazonDynamoDBClientBuilder
                .standard()
        amazonDynamoDBClientBuilder
                .setEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(amazoneDynamoDbEndpoint, amazoneDynamoDbRegion))

        return amazonDynamoDBClientBuilder.build()
    }

    @Bean
    fun dynamoDBMapper(): DynamoDBMapper {
        return DynamoDBMapper(amazonDynamoDB())
    }
}
