package kr.respectme.group.application.dto.notification

import kr.respectme.group.adapter.out.persistence.entity.notifications.JpaGroupNotification
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.domain.notifications.NotificationType
import kr.respectme.group.port.`in`.interfaces.vo.Writer
import java.time.Instant
import java.util.UUID

class NotificationDto(
    val notificationId: UUID,
    val groupId: UUID,
    val content: String,
    val type: NotificationType,
    val state: NotificationStatus,
    val scheduledAt: Instant?,
    val dayOfWeeks: Int?,
    val dayInterval: Int?,
    val createdAt: Instant,
    val updatedAt: Instant?,
    val lastSentAt: Instant?,
    val writer: Writer,
) {

    companion object {

        fun valueOf(entity: JpaGroupNotification): NotificationDto {
            return NotificationDto(
                notificationId = entity.identifier,
                groupId = entity.groupId,
                content = entity.content,
                writer = Writer(),
                type = entity.type,
                state = entity.status,
                scheduledAt = null,
                dayOfWeeks = null,
                dayInterval = null,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                lastSentAt = entity.lastSentAt,
            )
        }
    }
}