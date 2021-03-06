package com.uitnetwork.botadmin.config

import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import com.amazonaws.services.dynamodbv2.util.TableUtils.createTableIfNotExists
import com.uitnetwork.botadmin.model.CloudBotPermission
import mu.KotlinLogging
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
@EnableDynamoDBRepositories(basePackages = ["com.uitnetwork.botadmin.repository"])
class DynamoDBConfig(
        @Value("\${amazon.dynamodb.endpoint}") private val amazonDynamoDbEndpoint: String,
        @Value("\${amazon.dynamodb.region}") private val amazonDynamoDbRegion: String
) {
    companion object {
        private val logger = KotlinLogging.logger { }
        const val DEFAULT_READ_CAPACITY = 25L
        const val DEFAULT_WRITE_CAPACITY = 25L
    }

    @PostConstruct
    fun createTablesIfNotExist() {
        logger.info { "Creating DynamoDB tables if not exist to endpoint: $amazonDynamoDbEndpoint and region: $amazonDynamoDbRegion" }

        val dynamoDBMapper = DynamoDBMapper(amazonDynamoDB())
        val createTableRequest = dynamoDBMapper.generateCreateTableRequest(CloudBotPermission::class.java)
        createTableRequest.provisionedThroughput = ProvisionedThroughput(DEFAULT_READ_CAPACITY, DEFAULT_WRITE_CAPACITY)

        createTableIfNotExists(amazonDynamoDB(), createTableRequest)
    }

    @Bean
    fun amazonDynamoDB(): AmazonDynamoDB {
        val amazonDynamoDBClientBuilder = AmazonDynamoDBClientBuilder
                .standard()
        amazonDynamoDBClientBuilder
                .setEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(amazonDynamoDbEndpoint, amazonDynamoDbRegion))

        return amazonDynamoDBClientBuilder.build()
    }
}
