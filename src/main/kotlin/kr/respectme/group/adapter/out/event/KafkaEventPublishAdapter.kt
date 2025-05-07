package kr.respectme.group.adapter.out.event

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.group.port.out.event.EventPublishPort
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaEventPublishAdapter(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
): EventPublishPort {

    override fun publish(eventName: String, message: Any) {
        kafkaTemplate.send(eventName, objectMapper.writeValueAsString(message))
    }
}