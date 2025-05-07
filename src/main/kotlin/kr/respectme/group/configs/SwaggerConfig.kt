package kr.respectme.group.configs

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig(
    @Value("\${server.domain}")
    private val domain: String
) {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info().title("그룹 API Documentation")
                    .description("그룹 API 문서입니다.")
            )
            .components(Components().addSecuritySchemes("bearer-jwt",
                SecurityScheme().type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
            ))
            .addSecurityItem(SecurityRequirement().addList("bearer-jwt"))
            .addServersItem(Server().url(domain))
    }
}