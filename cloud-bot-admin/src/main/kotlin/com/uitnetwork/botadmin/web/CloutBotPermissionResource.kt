package com.uitnetwork.botadmin.web

import com.uitnetwork.botadmin.model.CloudBotPermission
import com.uitnetwork.botadmin.model.CloudBotPermissionId
import com.uitnetwork.botadmin.repository.CloudBotPermissionRepository
import mu.KotlinLogging
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cloud-bot-permissions")
class CloutBotPermissionResource(private val cloudBotPermissionRepository: CloudBotPermissionRepository) {
    companion object {
        private val logger = KotlinLogging.logger { }
    }

    @GetMapping
    fun getCloudBotPermissions(): List<CloudBotPermission> {
        return cloudBotPermissionRepository.findAll().toList()
    }

    @PostMapping
    fun createCloudBotPermission(@RequestBody cloudBotPermission: CloudBotPermission): CloudBotPermission {
        logger.info { "Creating $cloudBotPermission" }

        val existed = cloudBotPermissionRepository.existsById(cloudBotPermission.id)
        if (existed) throw CloudBotPermissionException("${cloudBotPermission.id} was already existed")

        return cloudBotPermissionRepository.save(cloudBotPermission)
    }

    @PutMapping
    fun updateCloudBotPermission(@RequestBody cloudBotPermission: CloudBotPermission): CloudBotPermission {
        logger.info { "Updating $cloudBotPermission" }

        val existed = cloudBotPermissionRepository.existsById(cloudBotPermission.id)
        if (!existed) throw CloudBotPermissionException("Resource not found: ${cloudBotPermission.id}")

        return cloudBotPermissionRepository.save(cloudBotPermission)
    }

    // TODO: combine path variables to composite object
    @GetMapping("/{source}/{userId}")
    fun getCloudBotPermission(@PathVariable("source") source: String, @PathVariable("userId") userId: String): CloudBotPermission {
        val id = CloudBotPermissionId(userId = userId, source = source)
        val cloudBotPermission = cloudBotPermissionRepository.findById(id).orElseThrow { CloudBotPermissionException("Resource not found: $id") }

        return cloudBotPermission
    }
}

class CloudBotPermissionException : RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
    constructor(message: String?, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean) : super(message, cause, enableSuppression, writableStackTrace)
}
