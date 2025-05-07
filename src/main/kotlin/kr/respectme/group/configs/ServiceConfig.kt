package kr.respectme.group.configs

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class ServiceConfig(
    @Value("\${respect-me.service-account.token}")
    val accessToken: String
) {

}