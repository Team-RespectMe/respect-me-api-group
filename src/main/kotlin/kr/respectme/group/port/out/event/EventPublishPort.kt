package kr.respectme.group.port.out.event

interface EventPublishPort {

    fun publish(eventName: String, message: Any)
}