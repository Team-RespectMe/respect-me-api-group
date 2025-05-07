package kr.respectme.group.port.`in`.saga

import kr.respectme.common.saga.SagaEvent
import kr.respectme.group.application.event.MemberDeleteSaga
import org.springframework.kafka.support.Acknowledgment

interface MemberDeleteSagaListenPort {

    fun memberDeleteSagaStart(message: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment)

    fun memberDeleteSagaCompleted(message: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment)

    fun memberDeleteSagaFailed(message: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment)
}