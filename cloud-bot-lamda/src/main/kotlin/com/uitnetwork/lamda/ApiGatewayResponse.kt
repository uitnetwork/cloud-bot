package com.uitnetwork.lamda

import com.fasterxml.jackson.databind.ObjectMapper
import java.util.*

class ApiGatewayResponse(
        val statusCode: Int = 200,
        var body: String = "",
        val headers: Map<String, String> = Collections.emptyMap()
) {
    companion object {
        private var objectMapper: ObjectMapper = ObjectMapper()
        fun ofResponse(response: Response): ApiGatewayResponse {
            return ApiGatewayResponse(body = objectMapper.writeValueAsString(response))
        }
    }
}
