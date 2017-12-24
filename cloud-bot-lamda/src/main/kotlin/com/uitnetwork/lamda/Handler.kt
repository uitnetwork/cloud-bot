package com.uitnetwork.lamda

import com.amazonaws.services.ec2.AmazonEC2ClientBuilder
import com.amazonaws.services.ec2.model.DescribeInstancesRequest
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import org.apache.logging.log4j.LogManager
import java.time.LocalDateTime.now

class Handler : RequestHandler<Map<String, Any>, ApiGatewayResponse> {
    companion object {
        private val LOG = LogManager.getLogger(Handler::class.java)
    }

    override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {
        LOG.info("received: " + input.keys.toString())


        val amazonEC2 = AmazonEC2ClientBuilder.defaultClient()
        var done = false
        var describeInstancesRequest = DescribeInstancesRequest()
        val stringBuilder = StringBuilder()
        while (!done) {
            LOG.info("Calling with nextToken: ${describeInstancesRequest.nextToken}")
            val describeInstancesResult = amazonEC2.describeInstances(describeInstancesRequest)

            for (reservation in describeInstancesResult.reservations) {
                for (instance in reservation.instances) {
                    stringBuilder.append("Reservation with id: ${instance.instanceId}, imageId: ${instance.imageId}, instanceType: ${instance.instanceType}, state: ${instance.state.name}, monitorState: ${instance.monitoring.state}")
                    stringBuilder.append(System.lineSeparator())
                }
            }

            describeInstancesRequest.nextToken = describeInstancesResult.nextToken
            if (describeInstancesResult.nextToken == null) {
                done = true;
            }
        }

        val response = BotResponse(stringBuilder.toString(), now())
        return ApiGatewayResponse.ofResponse(response)
    }
}
