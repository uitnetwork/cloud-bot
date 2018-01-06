package com.uitnetwork.bot.config

import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy


@Configuration
class AmazonEc2Config {
    companion object {
        private val logger = KotlinLogging.logger { }
    }

    @Bean
    @Lazy
    fun amazonEc2(): AmazonEC2 {
        logger.debug { "Creating AmazonEC2 to access Amazon EC2 API." }

        return AmazonEC2ClientBuilder.defaultClient()
    }
}
