package kr.respectme.group.adapter.out.event

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.group.port.out.event.EventPublishPort
import org.slf4j.LoggerFactory

//@Profile("local")
// TODO : @Profile("local") 어노테이션을 사용하여 로컬 환경에서만 사용하도록 설정
//@Component
class LocalEventPublishAdapter(private val objectMapper: ObjectMapper) : EventPublishPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun publish(eventName: String, data: Any) {
        logger.info("event published. ${eventName} : ${objectMapper.writeValueAsString(data)}")
    }
}