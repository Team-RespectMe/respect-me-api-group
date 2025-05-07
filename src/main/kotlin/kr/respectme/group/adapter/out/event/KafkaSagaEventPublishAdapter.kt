package kr.respectme.group.adapter.out.event

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.common.saga.SagaEvent
import kr.respectme.group.application.event.MemberDeleteSaga
import kr.respectme.group.common.saga.SagaDefinitions
import kr.respectme.group.port.out.event.SagaEventPublishPort
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class KafkaSagaEventPublishAdapter(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
): SagaEventPublishPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun <T: SagaEvent<*>> publish(eventName: String, message: T) {
        try {
            val record = ProducerRecord<String, String>(
                eventName,
                message.transactionId.toString(),
                objectMapper.writeValueAsString(message)
            )
            kafkaTemplate.send(record)
            logger.info("[${eventName}] published message to kafka, transactionId : ${message.transactionId}")
        } catch(e: Exception) {
            logger.error("[${eventName}] failed to publish message to kafka, transactionId : ${message.transactionId}")
            throw e
        }
    }
}