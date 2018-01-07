package com.uitnetwork.bot.service.ec2

import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.model.*
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class Ec2Service(private val amazonEc2: AmazonEC2) {
    companion object {
        private val logger = KotlinLogging.logger { }
    }

    fun getEc2Overview(): String {
        val allEc2Instances = getAllEc2Instances()
        val stringBuilder = StringBuilder("Ec2 Overview\n")
        allEc2Instances.forEach { stringBuilder.append("$it\n") }
        return stringBuilder.toString()
    }

    fun getAllEc2Instances(): List<Ec2Instance> {
        return getAllEc2InstancesMatchedRequest(DescribeInstancesRequest())
    }

    fun getEc2InstanceByInstanceName(instanceName: String): List<Ec2Instance> {
        val describeInstancesRequest = DescribeInstancesRequest().withFilters(Filter("tag:Name", listOf(instanceName)))
        return getAllEc2InstancesMatchedRequest(describeInstancesRequest)
    }

    private fun getAllEc2InstancesMatchedRequest(describeInstancesRequest: DescribeInstancesRequest): List<Ec2Instance> {
        var done = false
        val result = mutableListOf<Ec2Instance>()
        while (!done) {
            val describeInstancesResult = amazonEc2.describeInstances(describeInstancesRequest)

            for (reservation in describeInstancesResult.reservations) {
                for (instance in reservation.instances) {
                    result.add(mapToEc2Instance(instance))
                }
            }

            describeInstancesRequest.nextToken = describeInstancesResult.nextToken
            if (describeInstancesResult.nextToken == null) {
                done = true;
            }
        }

        return result.toList()
    }

    private fun mapToEc2Instance(instance: Instance): Ec2Instance {
        logger.debug { "Instance: $instance" }
        return Ec2Instance(
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

data class Ec2Instance(
        val id: String,
        val name: String,
        val type: String,
        val state: String
)

