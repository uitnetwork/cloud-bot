package com.uitnetwork.bot.config

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential.fromStream
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.compute.Compute
import com.google.api.services.compute.ComputeScopes.COMPUTE
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.get


@Configuration
class GoogleComputeConfig(private val env: Environment) {
    companion object {
        private val logger = KotlinLogging.logger { }
        const val GCP_SERVICE_KEY_FILENAME = "GCP_SERVICE_KEY_FILENAME"
        const val GCP_APP_NAME = "GCP_APP_NAME"
    }

    private val gcpServiceKeyFilename: String by lazy {
        env[GCP_SERVICE_KEY_FILENAME]
    }

    private val gcpAppName: String by lazy {
        env[GCP_APP_NAME]
    }

    @Bean
    fun compute(): Compute {
        logger.debug { "Creating Compute to access GCP Compute Engine API." }

        val netHttpTransport = newTrustedTransport()
        val gceCloutBotJsonInputstream = javaClass.classLoader.getResourceAsStream(gcpServiceKeyFilename)
        gceCloutBotJsonInputstream.use {
            var googleCredential = fromStream(gceCloutBotJsonInputstream)
            if (googleCredential.createScopedRequired()) {
                googleCredential = googleCredential.createScoped(listOf(COMPUTE))
            }

            val compute = Compute.Builder(netHttpTransport, JacksonFactory.getDefaultInstance(), googleCredential)
                    .setApplicationName(gcpAppName)
                    .build()

            return compute
        }
    }
}
