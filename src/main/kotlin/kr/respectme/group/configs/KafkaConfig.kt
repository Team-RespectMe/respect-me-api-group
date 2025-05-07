package kr.respectme.group.configs

import kr.respectme.group.common.saga.SagaDefinitions
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.support.converter.StringJsonMessageConverter

@Configuration
class KafkaConfig(@Value("\${respect-me.kafka.bootstrap-servers}") private val bootstrapServer: String) {
    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val configs: MutableMap<String, Any> = HashMap()
        configs[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServer
        return KafkaAdmin(configs)
    }

    @Bean
    fun memberDeleteSagaGroupServiceCompletedTopic(): NewTopic {
        val topicConfiguration = mutableMapOf<String, String>()
        topicConfiguration["cleanup.policy"] = "delete"
        topicConfiguration["retention.ms"] = "604800000"
        return NewTopic(SagaDefinitions.MEMBER_DELETE_SAGA_GROUP_SERVICE_COMPLETED, 1, 1)
            .configs(topicConfiguration)
    }

    @Bean
    fun memberDeleteSagaGroupServiceFailedTopic(): NewTopic {
        val topicConfiguration = mutableMapOf<String, String>()
        topicConfiguration["cleanup.policy"] = "delete"
        topicConfiguration["retention.ms"] = "604800000"
        return NewTopic(SagaDefinitions.MEMBER_DELETE_SAGA_GROUP_SERVICE_FAILED, 1, 1)
            .configs(topicConfiguration)
    }
}