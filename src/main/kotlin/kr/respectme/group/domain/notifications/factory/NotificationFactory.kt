package kr.respectme.group.domain.notifications.factory

import kr.respectme.group.application.dto.notification.NotificationCreateCommand
import kr.respectme.group.domain.notifications.Notification

interface NotificationFactory {

    fun build(command: NotificationCreateCommand): Notification
}