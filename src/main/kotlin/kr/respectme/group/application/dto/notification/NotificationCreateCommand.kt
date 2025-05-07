package kr.respectme.group.application.dto.notification

import kr.respectme.group.application.attachment.LinkAttachmentCommand
import kr.respectme.group.domain.notifications.DayOfWeek
import kr.respectme.group.domain.notifications.NotificationType
import kr.respectme.group.port.`in`.interfaces.dto.NotificationCreateRequest
import java.time.Instant
import java.time.OffsetTime
import java.time.ZoneOffset
import java.util.UUID

data class NotificationCreateCommand(
    val groupId: UUID,
    val memberId: UUID,
    val content: String,
    val type: NotificationType,
    val scheduledAt: Instant?,
//    val triggerTime: OffsetTime?,
    val dayOfWeeks: List<DayOfWeek>?,
    val dayInterval: Int?,
) {

    companion object {

        fun valueOf(groupId: UUID, memberId: UUID, request: NotificationCreateRequest): NotificationCreateCommand {
            return NotificationCreateCommand(
                groupId = groupId,
                memberId = memberId,
                content = request.content,
                type = request.type,
                scheduledAt = null,
                dayOfWeeks = null,
                dayInterval = null
            )
        }
    }
}