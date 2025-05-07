package kr.respectme.group.application.dto.notification

import kr.respectme.group.domain.notifications.DayOfWeek
import kr.respectme.group.domain.notifications.NotificationType
import kr.respectme.group.port.`in`.interfaces.dto.NotificationModifyRequest
import java.time.Instant
import java.util.*

data class NotificationModifyCommand(
    val content: String? = null,
    val type: NotificationType? = null,
    val scheduledAt: Instant? = null,
    val dayOfWeek: List<DayOfWeek>? = null,
    val dayInterval: Int? = null
) {

    companion object {

        fun valueOf(request: NotificationModifyRequest): NotificationModifyCommand {
            return NotificationModifyCommand(
                content = request.content
            )
        }
    }
}