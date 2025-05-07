package kr.respectme.group.port.out.persistence

import kr.respectme.group.domain.notifications.Notification
import java.util.UUID

interface SaveNotificationPort {

    fun saveNotification(entity: Notification): Notification

    fun deleteNotification(id: UUID)

    fun deleteNotification(entity: Notification)
}