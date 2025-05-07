package kr.respectme.group.adapter.out.persistence.entity

import kr.respectme.group.domain.notifications.Notification
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.domain.notifications.NotificationType
import kr.respectme.group.port.`in`.interfaces.vo.NotificationGroupVo
import kr.respectme.group.port.`in`.interfaces.vo.Writer
import java.time.Instant
import java.util.UUID

data class GroupNotificationQueryModel(
    val id: UUID= UUID.randomUUID(),
    val type: NotificationType = NotificationType.IMMEDIATE,
    val groupInfo: NotificationGroupVo,
    val status: NotificationStatus = NotificationStatus.PENDING,
    val writer: Writer = Writer(),
    val content: String="",
    val scheduledAt: Instant? = null,
    val dayOfWeeks: Int? = null,
    val dayInterval: Int? = null,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant? = null,
    val lastSentAt: Instant? = null,
) {}