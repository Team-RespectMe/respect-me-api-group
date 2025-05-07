package kr.respectme.group.common.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.respectme.common.error.exporter.ErrorExporter
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

@Component
@Profile("test")
class LoggingFilter(
    private val messageExporter: ErrorExporter
) : OncePerRequestFilter() {
    private val pathMatcher = AntPathMatcher()
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val wrappedRequest = ContentCachingRequestWrapper(request)
        val wrappedResponse = ContentCachingResponseWrapper(response)

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse)
        } finally {
            logRequestAndResponse(wrappedRequest, wrappedResponse)
            wrappedResponse.copyBodyToResponse()

        }
    }

    private fun logRequestAndResponse(
        request: ContentCachingRequestWrapper,
        response: ContentCachingResponseWrapper,
    ) {
        logger.info("request\nuri : ${request.requestURI}\nauthorization: ${request.getHeader("Authorization")}\nbody : ${request.contentAsByteArray.toString(Charsets.UTF_8)}")
        logger.info("response body : ${response.contentAsByteArray.toString(Charsets.UTF_8)}")

        if(pathMatcher.match("/api/**", request.requestURI) && response.status >= 400) {
            messageExporter.export(request, response)
        }
    }

}