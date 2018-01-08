package com.uitnetwork.botadmin.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.uitnetwork.botadmin.model.CloudBotPermission
import com.uitnetwork.botadmin.repository.CloudBotPermissionRepository
import mu.KotlinLogging
import org.springframework.util.Assert
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cloud-bot-permission")
class CloutBotPermissionResource(private val cloudBotPermissionRepository: CloudBotPermissionRepository) {
    companion object {
        private val logger = KotlinLogging.logger { }
    }

    //    @PostConstruct
    fun init() {
        val cloudBotPermission = CloudBotPermission(userId = "U0T96K5Q9", source = "SLACK",
                permissions = listOf("OVERVIEW", "START", "STOP"))
        cloudBotPermissionRepository.save(cloudBotPermission)
    }

    @GetMapping
    fun getCloudBotPermissions(): List<CloudBotPermission>? {
        return cloudBotPermissionRepository.findAll().toList()
    }

    @PostMapping
    fun createCloudBotPermission(@RequestBody cloudBotPermission: CloudBotPermission): CloudBotPermission {
        Assert.isNull(cloudBotPermission.id, "Id must be null for new CloudBotPermission")

        return cloudBotPermissionRepository.save(cloudBotPermission)
    }

    @PutMapping
    fun updateCloudBotPermission(@RequestBody cloudBotPermission: CloudBotPermission): CloudBotPermission {
        Assert.notNull(cloudBotPermission.id, "Id must be not null for to update a CloudBotPermission")

        return cloudBotPermissionRepository.save(cloudBotPermission)
    }

    @GetMapping("/{id}")
    fun getCloudBotPermission(@PathVariable("id") id: String): CloudBotPermission {
        val cloudBotPermission = cloudBotPermissionRepository.findById(id).orElseThrow { RuntimeException("Resource not found: $id") }

        // DEMO purpose
        logger.info { "CloudBotPermission: ${cloudBotPermission} with sourceLength: ${cloudBotPermission.source.length}" }

        // DEMO purpose
        val sourceLength = cloudBotPermission.source.length
        logger.info { "Length: $sourceLength" }

        return cloudBotPermission
    }

}
