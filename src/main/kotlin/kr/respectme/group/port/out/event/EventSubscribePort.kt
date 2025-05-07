package kr.respectme.group.port.out.event

import kr.respectme.group.port.out.event.dto.NotificationSentEvent

interface EventSubscribePort {

    fun onReceiveNotificationSent(event: NotificationSentEvent)
}