package kr.respectme.group.infrastructures.discord

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponseWrapper
import kr.respectme.common.error.exporter.ErrorExporter
import org.slf4j.LoggerFactory
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Component
class DiscordMessageExporter(
    private val objectMapper: ObjectMapper,
    private val discordFeignClient: DiscordFeignClient
): ErrorExporter {
//https://discord.com/api/webhooks/1310337305482887219/EJH13ZVmfy9VglSzOME3zGvtJcfnpVjHbIl1Y-2TYTmD_pX5zXV0DRExv4xiQbhkAIUV

    private val formatter = DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC)

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun export(request: ContentCachingRequestWrapper, response: ContentCachingResponseWrapper) {
        val time = Instant.now()
        val embeds = listOf(
            Embed(
                title = "Group API Error Information",
                description = """
### :alarm_clock:Error Time  
                    ${formatter.format(time)}
### :inbox_tray:Request
                    - Content Type: ${request.contentType}  
                    - Method: ${request.method}  
                    - URI: ${request.requestURI}  
                    - Remote Address: ${request.remoteAddr}  
                    - User Agent: ${request.getHeader("User-Agent")}
                    - Authorization: ${request.getHeader("Authorization")}
                    - Request Body : ${request.contentAsByteArray.toString(Charsets.UTF_8)}
                    - Parameters: ${request.queryString}
### :outbox_tray:Response
                    - Status: ${response.status}
                    - Response Body: ${response.contentAsByteArray.toString(Charsets.UTF_8)}
                   """.trimIndent())
            )

        val dto = DiscordMessageDto("Group API Error Log", embeds)
        logger.debug(objectMapper.writeValueAsString(dto))
        discordFeignClient.sendMessage(dto)
    }
}