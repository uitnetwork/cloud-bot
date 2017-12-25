package com.uitnetwork.bot.config

import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class AmazonEc2Config {

    @Bean
    fun amazonEc2(): AmazonEC2 {
        return AmazonEC2ClientBuilder.defaultClient()
    }
}
