package kr.respectme.group.configs
//
import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.common.security.handler.EntrypointUnauthorizedHandler
import kr.respectme.common.security.handler.JwtAccessDeniedHandler
import kr.respectme.common.security.jwt.JwtAuthenticationFilter
import kr.respectme.common.security.jwt.JwtAuthenticationProvider
import kr.respectme.common.security.jwt.adapter.JwtAuthenticationAdapter
import kr.respectme.common.security.jwt.port.JwtAuthenticationPort
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.ExceptionTranslationFilter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.header.writers.frameoptions.WhiteListedAllowFromStrategy
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebSecurity
class SecurityConfig(
    @Value("\${respect-me.msa.auth-api.url}")
    private val authUrl: String,
    private val objectMapper: ObjectMapper,
    @Value("\${server.cors.allowed-origins}")
    private val allowedOriginsString: String,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val allowedOrigins: List<String> by lazy {
        allowedOriginsString.split(",")
    }

    fun excludePathMatcher(): RequestMatcher {
        val pathMatchers = listOf(
//            AntPathRequestMatcher("/api/v1/notification-groups", "POST"),
//            AntPathRequestMatcher("/api/v1/notification-groups", "GET"),
            AntPathRequestMatcher("/static/**"),
            AntPathRequestMatcher("/swagger-ui/**"),
            AntPathRequestMatcher("/v3/**"),
            AntPathRequestMatcher("/api-docs/**"),
        )
        return OrRequestMatcher(*pathMatchers.toTypedArray())
    }

    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(excludePathMatcher(), jwtAuthenticationProvider, objectMapper)
    }
    //
    @Bean
    fun unauthorizedEntryPoint(): EntrypointUnauthorizedHandler {
        return EntrypointUnauthorizedHandler()
    }

    @Bean
    fun accessDeniedHandler(): JwtAccessDeniedHandler {
        return JwtAccessDeniedHandler()
    }

    @Bean
    @ConditionalOnProperty(name = ["spring.h2.console.enabled"],havingValue = "true")
    fun ignoringWebSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer {
            it.ignoring().requestMatchers(PathRequest.toH2Console())
        }
    }

    @Bean
    fun corsConfig(): CorsConfiguration {
        val config = CorsConfiguration()
        config.allowedOrigins = allowedOrigins;
        config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS");
        config.allowedHeaders = listOf("*");
        return config;
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig())
        return source
    }

    @Bean
    fun corsFilter(): CorsFilter {
        return CorsFilter(corsConfigurationSource())
    }

    @Bean
    fun httpSecurity(http: HttpSecurity): SecurityFilterChain {
        logger.debug("httpSecurity called.")
        return http.httpBasic { it.disable() }
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .cors{ it -> it.configurationSource(corsConfigurationSource()) }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterAfter(jwtAuthenticationFilter(), ExceptionTranslationFilter::class.java)
            .exceptionHandling{ it ->
                it.accessDeniedHandler(accessDeniedHandler())
                it.authenticationEntryPoint(unauthorizedEntryPoint())
            }
            .authorizeHttpRequests {
                it.requestMatchers("/notification-groups/swagger-ui.html",
                    "/notification-groups/v3/api-docs", "/notification-groups/swagger-ui/**",
                    "/notification-groups/v3/api-docs/**"
                ).permitAll()
                it.requestMatchers("/internal/**").hasRole("SERVICE")
                it.requestMatchers("/api/v1/**").hasAnyRole("MEMBER", "ADMIN", "SERVICE")
                it.anyRequest().permitAll()
            }
            .build()
    }
}