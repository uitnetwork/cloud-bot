package com.uitnetwork.bot.service

import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.model.*
import com.uitnetwork.bot.model.Ec2Info
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class Ec2Service(private val amazonEc2: AmazonEC2) {
    companion object {
        private val logger = KotlinLogging.logger { }
    }

    fun getAllEc2Info(): List<Ec2Info> {
        return getAllEc2InfoMatchedRequest(DescribeInstancesRequest())
    }

    fun getEc2InfoByInstanceName(instanceName: String): List<Ec2Info> {
        val describeInstancesRequest = DescribeInstancesRequest().withFilters(Filter("tag:Name", listOf(instanceName)))
        return getAllEc2InfoMatchedRequest(describeInstancesRequest)
    }

    private fun getAllEc2InfoMatchedRequest(describeInstancesRequest: DescribeInstancesRequest): List<Ec2Info> {
        var done = false
        val result = mutableListOf<Ec2Info>()
        while (!done) {
            val describeInstancesResult = amazonEc2.describeInstances(describeInstancesRequest)

            for (reservation in describeInstancesResult.reservations) {
                for (instance in reservation.instances) {
                    result.add(mapToEc2Info(instance))
                }
            }

            describeInstancesRequest.nextToken = describeInstancesResult.nextToken
            if (describeInstancesResult.nextToken == null) {
                done = true;
            }
        }

        return result.toList()
    }

    private fun mapToEc2Info(instance: Instance): Ec2Info {
        logger.debug { "Instance: $instance" }
        return Ec2Info(
                id = instance.instanceId,
                name = instance.tags.filter { it.key == "Name" }.firstOrNull()?.value ?: "NO_NAME",
                type = instance.instanceType,
                state = instance.state.name
        )
    }

    fun startEc2Instance(vararg instanceId: String) {
        val startInstancesRequest = StartInstancesRequest().withInstanceIds(*instanceId)
        val startInstancesResult = amazonEc2.startInstances(startInstancesRequest)

        logger.info { "Starting instances: $instanceId with result: $startInstancesResult" }
    }

    fun stopEc2Instance(vararg instanceId: String) {
        val stopInstancesRequest = StopInstancesRequest().withInstanceIds(*instanceId)
        val stopInstancesResult = amazonEc2.stopInstances(stopInstancesRequest)

        logger.info { "Stopping instances: $instanceId with result: $stopInstancesResult" }
    }
}
