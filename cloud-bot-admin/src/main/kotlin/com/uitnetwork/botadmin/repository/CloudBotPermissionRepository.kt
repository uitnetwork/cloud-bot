package com.uitnetwork.botadmin.repository

import com.uitnetwork.botadmin.model.CloudBotPermission
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.CrudRepository

@EnableScan
interface CloudBotPermissionRepository : CrudRepository<CloudBotPermission, String> {
}
