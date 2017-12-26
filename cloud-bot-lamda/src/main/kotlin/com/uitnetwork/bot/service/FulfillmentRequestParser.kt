package com.uitnetwork.bot.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.uitnetwork.bot.exception.FulfillmentRequestParsingException
import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.model.Source
import com.uitnetwork.bot.model.Source.SKYPE
import com.uitnetwork.bot.model.Source.SLACK
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class FulfillmentRequestParser(val objectMapper: ObjectMapper) {

    fun parse(requestBody: String): FulfillmentRequest {
        val jsonNode = objectMapper.readTree(requestBody)

        val source = parseSource(jsonNode)
        val userId = parseUserId(jsonNode, source)
        val userText = parseUserText(jsonNode)
        val action = parseAction(jsonNode)
        val params = parseParams(jsonNode)
        val requestTime = parseRequestTime(jsonNode)
        val requestId = parseRequestId(jsonNode)

        return FulfillmentRequest(source = source, userId = userId, userText = userText, action = action,
                params = params, requestTime = requestTime, requestId = requestId)
    }

    private fun parseSource(jsonNode: JsonNode): Source {
        val sourceAsString = jsonNode.at("/originalRequest/source").textValue()
        return when (sourceAsString) {
            "skype" -> SKYPE
            "slack_testbot", "slack" -> SLACK
            else -> throw FulfillmentRequestParsingException("Source: $sourceAsString is not supported.")
        }
    }

    private fun parseUserId(jsonNode: JsonNode, source: Source): String {
        return when (source) {
            SKYPE -> jsonNode.at("/originalRequest/data/user/id").textValue()
            SLACK -> jsonNode.at("/originalRequest/data/user").textValue()
        }
    }

    private fun parseUserText(jsonNode: JsonNode): String {
        return jsonNode.at("/originalRequest/data/text").textValue()
    }

    private fun parseAction(jsonNode: JsonNode): String {
        return jsonNode.at("/result/action").textValue()
    }

    private fun parseParams(jsonNode: JsonNode): Map<String, String> {
        val paramsNode = jsonNode.at("/result/parameters")
        val params = mutableMapOf<String, String>()
        for (field in paramsNode.fields()) {
            params.put(field.key, field.value.textValue())
        }
        return params.toMap()
    }

    private fun parseRequestTime(jsonNode: JsonNode): ZonedDateTime {
        val timestamp = jsonNode.at("/timestamp").textValue()
        return ZonedDateTime.parse(timestamp)
    }

    private fun parseRequestId(jsonNode: JsonNode): String {
        return jsonNode.at("/id").textValue()
    }
}


