package com.uitnetwork.bot.service.gce

import com.google.api.services.compute.Compute
import com.google.api.services.compute.model.Instance
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service
import java.math.BigInteger

@Service
class GceService(private val compute: Compute, private val env: Environment) {
    companion object {
        const val GCP_PROJECT_ID = "GCP_PROJECT_ID"
        const val GCP_GCE_ZONE = "GCP_GCE_ZONE"
    }

    private val gcpProjectId: String by lazy {
        env[GCP_PROJECT_ID]
    }

    private val gcpGceZone: String by lazy {
        env[GCP_GCE_ZONE]
    }

    fun getGceOverview(): String {

        val allGceInstances = getAllGceInstancesInProjectAndZone(gcpProjectId, gcpGceZone)

        val stringBuilder = StringBuilder("Compute Engine Overview\n")
        allGceInstances.forEach { stringBuilder.append("$it\n") }

        return stringBuilder.toString()
    }

    fun getAllGceInstancesInProjectAndZone(projectId: String, zone: String): List<GceInstance> {
        val computeInstancesListRequest = compute.instances().list(projectId, zone)

        val instanceList = computeInstancesListRequest.execute()
        if (instanceList.items == null) {
            return emptyList()
        }

        return instanceList.items.map { mapToGceInstance(it) }
    }

    private fun mapToGceInstance(instance: Instance): GceInstance {
        val lastIndexOfSlash = instance.machineType.lastIndexOf("/")
        val machineType = instance.machineType.substring(lastIndexOfSlash + 1)
        return GceInstance(id = instance.id,
                name = instance.name,
                machineType = machineType,
                status = instance.status)
    }

    fun startGceInstance(instanceName: String) {
        val computeInstancesStartRequest = compute.instances().start(gcpProjectId, gcpGceZone, instanceName)
        computeInstancesStartRequest.execute()
    }

    fun stopGceInstance(instanceName: String) {
        val computeInstancesStopRequest = compute.instances().stop(gcpProjectId, gcpGceZone, instanceName)
        computeInstancesStopRequest.execute()
    }
}

data class GceInstance(
        val id: BigInteger,
        val name: String,
        val machineType: String,
        val status: String
)
