package kr.respectme.group.infrastructures.discord

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@FeignClient(name = "discord-client", url = "\${respect-me.error-exporter.discord.url}")
interface DiscordFeignClient {

    @PostMapping
    fun sendMessage(@RequestBody message: DiscordMessageDto)
}