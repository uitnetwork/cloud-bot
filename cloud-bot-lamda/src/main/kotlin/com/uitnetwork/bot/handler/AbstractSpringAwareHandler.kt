package com.uitnetwork.bot.handler

import com.amazonaws.services.lambda.runtime.RequestHandler
import com.uitnetwork.bot.model.ApiGatewayResponse
import mu.KotlinLogging
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import java.lang.reflect.ParameterizedType


abstract class AbstractSpringAwareHandler<T> : RequestHandler<Map<String, Any>, ApiGatewayResponse> {
    companion object {
        private val logger = KotlinLogging.logger { }
    }

    val applicationContext: ApplicationContext

    constructor() {
        val typeParameterClass = (javaClass.genericSuperclass as ParameterizedType).getActualTypeArguments()[0] as Class<*>
        applicationContext = AnnotationConfigApplicationContext(typeParameterClass)

        logger.info { "Created ${javaClass.simpleName} instance: $this." }
    }
}
