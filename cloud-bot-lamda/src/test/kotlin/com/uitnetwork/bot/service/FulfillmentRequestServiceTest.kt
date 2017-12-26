package com.uitnetwork.bot.service

import com.uitnetwork.bot.config.ObjectMapperConfig
import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.model.Source.SKYPE
import com.uitnetwork.bot.model.Source.SLACK
import org.apache.commons.io.IOUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.ZonedDateTime

class FulfillmentRequestServiceTest {

    private val fulfillmentRequestService = FulfillmentRequestService(ObjectMapperConfig().objectMapper())

    private val slackSampleRequest by lazy {
        IOUtils.toString(javaClass.getResourceAsStream("/slack_sample_request.json"), "UTF-8")
    }

    private val slackSampleRequestWithParams by lazy {
        IOUtils.toString(javaClass.getResourceAsStream("/slack_sample_request_with_params.json"), "UTF-8")
    }

    private val skypeSampleRequest by lazy {
        IOUtils.toString(javaClass.getResourceAsStream("/skype_sample_request.json"), "UTF-8")
    }

    @Test
    fun shouldParseSlackRequestCorrectly() {
        val fulfillmentRequest = fulfillmentRequestService.parse(slackSampleRequest)

        assertThat(fulfillmentRequest).isEqualTo(FulfillmentRequest(source = SLACK, userId = "testUser", userText = "ec2 overview", action = "Test",
                requestTime = ZonedDateTime.parse("2017-12-26T06:32:39.324Z"), requestId = "cc9633ee-1685-4041-b267-d488b04a8ca5"))
    }

    @Test
    fun shouldParseSlackRequestWithParamsCorrectly() {
        val fulfillmentRequest = fulfillmentRequestService.parse(slackSampleRequestWithParams)

        assertThat(fulfillmentRequest).isEqualTo(FulfillmentRequest(source = SLACK, userId = "testUser", userText = "ec2 overview i-023bdcd9a310ae47b", action = "Test",
                params = mapOf("ec2Id" to "i-023bdcd9a310ae47b"), requestTime = ZonedDateTime.parse("2017-12-26T09:59:58.322Z"), requestId = "405242dd-d832-49eb-ae8a-e0aa762695dc"))
    }

    @Test
    fun shouldParseSkypeRequestCorrectly() {
        val fulfillmentRequest = fulfillmentRequestService.parse(skypeSampleRequest)

        assertThat(fulfillmentRequest).isEqualTo(FulfillmentRequest(source = SKYPE, userId = "testId", userText = "ec2 overview", action = "Test",
                requestTime = ZonedDateTime.parse("2017-12-25T11:32:59.387Z"), requestId = "5d45c492-f4f3-475e-b9f6-eea8d894b8af"))
    }

}
