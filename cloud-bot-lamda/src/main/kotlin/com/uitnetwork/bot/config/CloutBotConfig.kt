package com.uitnetwork.bot.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(value = ["com.uitnetwork.bot.config", "com.uitnetwork.bot.service"], lazyInit = true)
class CloutBotConfig
