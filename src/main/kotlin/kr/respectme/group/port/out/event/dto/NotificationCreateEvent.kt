package kr.respectme.group.port.out.event.dto

import kr.respectme.group.domain.NotificationGroup
import kr.respectme.group.domain.notifications.Notification
import java.time.Instant
import java.util.UUID

data class NotificationCreateEvent(
    val notificationId: UUID,
    val groupId: UUID,
    val groupName: String,
    val senderId: UUID,
    val title: String? = null,
    val receiverIds: List<UUID>,
    val contents: String,
    val createdAt: Instant
) {

    companion object {
        val eventName = "notification-create-event"

        fun valueOf(group: NotificationGroup, notification: Notification, receiverIds: List<UUID>): NotificationCreateEvent {
            return NotificationCreateEvent(
                notificationId = notification.id,
                groupId = notification.getGroupId(),
                groupName = group.getName(),
                senderId = notification.getSenderId(),
                title = "",
                receiverIds = receiverIds,
                contents = notification.getContent(),
                createdAt = notification.getCreatedAt()
            )
        }
    }
}