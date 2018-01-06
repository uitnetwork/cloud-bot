package com.uitnetwork.bot.service

import com.google.api.services.compute.Compute
import org.springframework.stereotype.Service

@Service
class GoogleComputeService(private val compute: Compute) {
    companion object {
        const val PROJECT_ID = "uitnetwork"
        const val ZONE = "asia-southeast1-a"
    }

    fun getComputeOverview(): String {
        val computeInstancesListRequest = compute.instances().list(PROJECT_ID, ZONE)

        val instanceList = computeInstancesListRequest.execute()

        val stringBuilder = StringBuilder("Compute Engine Overview\n")
        instanceList.items.forEach { stringBuilder.append("${it.name},  ${it.name}, ${it.machineType}, ${it.status}\n") }

        return stringBuilder.toString()
    }

    fun startComputeInstance(instanceName: String) {
        val computeInstancesStartRequest = compute.instances().start(PROJECT_ID, ZONE, instanceName)
        computeInstancesStartRequest.execute()
    }

    fun stopComputeInstance(instanceName: String) {
        val computeInstancesStopRequest = compute.instances().stop(PROJECT_ID, ZONE, instanceName)
        computeInstancesStopRequest.execute()
    }
}
