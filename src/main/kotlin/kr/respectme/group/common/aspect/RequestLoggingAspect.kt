package kr.respectme.group.common.aspect

import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Component
@Aspect
class RequestLoggingAspect {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Before("execution(* kr.respectme.group.interfaces.*.*.*(..))")
    fun logRequest() {
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        val tz = request.getHeader("X-TimeZone")

        if(request is HttpServletRequest) {
            val requestWrapper = RequestWrapper(request)
            logger.info("TimeZone: ${tz}, requestUrl : ${request.requestURL} Request body: ${requestWrapper.getBody()}")
        }
    }
}