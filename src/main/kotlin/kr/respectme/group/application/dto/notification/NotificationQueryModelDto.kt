package kr.respectme.group.application.dto.notification

import kr.respectme.group.adapter.out.persistence.entity.GroupNotificationQueryModel
import kr.respectme.group.domain.notifications.DayOfWeek
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.domain.notifications.NotificationType
import kr.respectme.group.port.`in`.interfaces.vo.NotificationGroupVo
import kr.respectme.group.port.`in`.interfaces.vo.Writer
import java.time.Instant
import java.util.UUID

data class NotificationQueryModelDto(
    val id: UUID = UUID.randomUUID(),
    val type: NotificationType = NotificationType.IMMEDIATE,
    val groupInfo: NotificationGroupVo,
    val status: NotificationStatus = NotificationStatus.PENDING,
    val writer: Writer = Writer(),
    val content: String = "",
    val scheduledAt: Instant? = null,
    val dayOfWeeks: List<DayOfWeek> = emptyList(),
    val dayInterval: Int? = null,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant? = null,
    val lastSentAt: Instant? = null,
) {

    companion object {

        fun valueOf(model: GroupNotificationQueryModel): NotificationQueryModelDto {
            return NotificationQueryModelDto(
                id = model.id,
                type = model.type,
                groupInfo = model.groupInfo,
                status = model.status,
                writer = model.writer,
                content = model.content,
                scheduledAt = model.scheduledAt,
                dayOfWeeks = DayOfWeek.toList(model.dayOfWeeks),
                dayInterval = model.dayInterval,
                createdAt = model.createdAt,
                updatedAt = model.updatedAt,
                lastSentAt = model.lastSentAt
            )
        }
    }
}