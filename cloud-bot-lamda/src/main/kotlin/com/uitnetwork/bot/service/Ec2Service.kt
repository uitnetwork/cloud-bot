package com.uitnetwork.bot.service

import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.model.DescribeInstancesRequest
import com.amazonaws.services.ec2.model.Instance
import com.uitnetwork.bot.model.Ec2Info
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service

@Service
class Ec2Service(private val amazonEc2: AmazonEC2) {
    companion object {
        private val logger = LogManager.getLogger(Ec2Service::class.java)
    }

    fun getAllEc2Info(): List<Ec2Info> {
        var done = false
        val describeInstancesRequest = DescribeInstancesRequest()
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
        return Ec2Info(
                id = instance.instanceId,
                name = instance.tags.filter { it.key == "Name" }.firstOrNull()?.value ?: "NO_NAME",
                type = instance.instanceType,
                state = instance.state.name
        )
    }
}
