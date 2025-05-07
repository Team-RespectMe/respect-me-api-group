package kr.respectme.group.adapter.`in`.saga

import kr.respectme.common.saga.SagaEvent
import kr.respectme.common.saga.SagaEventHandler
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupMemberRepository
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupRepository
import kr.respectme.group.application.event.MemberDeleteSaga
import kr.respectme.group.common.saga.SagaDefinitions
import kr.respectme.group.port.`in`.saga.MemberDeleteSagaListenPort
import kr.respectme.group.port.out.event.SagaEventPublishPort
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class KafkaMemberDeleteSagaListenAdapter(
    private val memberDeleteSagaEventHandler: SagaEventHandler<SagaEvent<MemberDeleteSaga>>,
    private val eventPublishPort: SagaEventPublishPort,
    private val groupRepository: JpaGroupRepository,
    private val groupMemberRepository: JpaGroupMemberRepository
): MemberDeleteSagaListenPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["member-delete-saga"])
    @Transactional
    override fun memberDeleteSagaStart(message: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment) {
        try {
            logger.info("[member-delete-saga] member delete saga transactionId: ${message.transactionId} started. member id: ${message.data?.memberId}")
            memberDeleteSagaEventHandler.handleEvent(message)
            eventPublishPort.publish(SagaDefinitions.MEMBER_DELETE_SAGA_GROUP_SERVICE_COMPLETED, message)
        } catch(e: Exception) {
            logger.error("[member-delete-saga] transactionId: ${message.transactionId} failed. message : ${e.message}")
            eventPublishPort.publish(SagaDefinitions.MEMBER_DELETE_SAGA_GROUP_SERVICE_FAILED, message)
        } finally {
            commit(acknowledgment)
        }
    }

    @KafkaListener(topics = ["member-delete-completed-saga"])
    @Transactional
    override fun memberDeleteSagaCompleted(message: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment) {
        try {
            logger.info("[member-delete-completed-saga] member delete saga transactionId: ${message.transactionId} completed. member id: ${message.data?.memberId} delete all group members and owned groups")
            // TODO 하드 딜리트 로직은 고려 중
            groupMemberRepository.deleteAllByMemberId(message.data?.memberId!!)
            groupRepository.deleteByOwnerId(message.data?.memberId!!)
            logger.info("[member-delete-completed-saga] transactionId: ${message.transactionId} completed.")
        } catch (e: Exception) {
            logger.error("[member-delete-completed-saga] transactionId: ${message.transactionId} failed. message : ${e.message}")
        } finally {
            commit(acknowledgment)
        }
    }

    @KafkaListener(topics = ["member-delete-failed-saga"])
    @Transactional
    override fun memberDeleteSagaFailed(message: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment) {
        try {
            logger.info("[member-delete-failed-saga] member delete saga transactionId: ${message.transactionId} failed. member id: ${message.data?.memberId} restore to isDeleted false and owned groups also")
            memberDeleteSagaEventHandler.compensate(message)
        } catch (e: Exception) {
            // 보상 로직이 실패하였다는 것은 수동 개입이 필요하다는 의미이다.
            logger.error("[member-delete-failed-saga][FATAL] transactionId: ${message.transactionId} failed!, error message : ${e.message}")
            logger.error("[member-delete-failed-saga][FATAL] manual intervention required. member id: ${message.data?.memberId} should be restored manually.")
        } finally {
            commit(acknowledgment)
        }
    }

    @Transactional
    private fun commit(acknowledgment: Acknowledgment) {
        acknowledgment.acknowledge()
    }
}