package kr.respectme.group.port.out.event

import kr.respectme.common.saga.SagaEvent

interface SagaEventPublishPort {

    fun <T: SagaEvent<*>> publish(eventName: String, message: T)
}