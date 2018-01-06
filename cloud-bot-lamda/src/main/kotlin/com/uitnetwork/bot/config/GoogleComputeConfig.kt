package com.uitnetwork.bot.config

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential.fromStream
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.compute.Compute
import com.google.api.services.compute.ComputeScopes.COMPUTE
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class GoogleComputeConfig {
    companion object {
        private val logger = KotlinLogging.logger { }
        const val GCE_CLOUD_BOT = "gce-cloud-bot.json"
        const val APP_NAME = "cloud-bot-lamda"
    }

    @Bean
    fun compute(): Compute {
        logger.debug { "Creating Compute to access GCP Compute Engine API." }

        val netHttpTransport = newTrustedTransport()
        val gceCloutBotJsonInputstream = javaClass.classLoader.getResourceAsStream(GCE_CLOUD_BOT)
        gceCloutBotJsonInputstream.use {
            var googleCredential = fromStream(gceCloutBotJsonInputstream)
            if (googleCredential.createScopedRequired()) {
                googleCredential = googleCredential.createScoped(listOf(COMPUTE))
            }

            val compute = Compute.Builder(netHttpTransport, JacksonFactory.getDefaultInstance(), googleCredential)
                    .setApplicationName(APP_NAME)
                    .build()

            return compute
        }
    }
}
