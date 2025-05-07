package kr.respectme.group.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.respectme.group.application.dto.notification.NotificationCreateResult
import kr.respectme.group.domain.notifications.DayOfWeek
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.domain.notifications.NotificationType
import java.time.Instant
import java.util.*

@Schema(description = "Notification 생성/수정 응답 객체")
data class NotificationCommandResponse(
    @Schema(description = "Notification 대상 그룹 ID")
    val groupId: UUID,
    @Schema(description = "Notification 발신자 ID")
    val senderId: UUID,
    @Schema(description = "Notification ID")
    val notificationId: UUID,
    @Schema(description = "Notification 상태", example = "PENDING")
    val state: NotificationStatus,
    @Schema(description = "Notification 타입", examples = ["IMMEDIATE", "SCHEDULED", "REPEATED_WEEKLY", "REPEATED_INTERVAL"])
    val type: NotificationType,
    @Schema(description = "Notification 내용")
    val content: String,
    @Schema(description = "Notification 생성 일시")
    val createdAt: Instant,
    @Schema(description = "Notification 마지막 전송 일시")
    val lastSentAt: Instant? = null,
    @Schema(description = "Notification 예약 일시")
    val scheduledAt: Instant? = null,
    @Schema(description = "Notification 요일, 매주 반복일 경우(TYPE = REPEATED_WEEKLY)")
    val dayOfWeeks: List<DayOfWeek>? = null,
    @Schema(description = "Notification 간격, 매 n일 반복일 경우(TYPE = REPEATED_INTERVAL)")
    val dayInterval: Int? = null,
) {
    companion object {
        fun valueOf(result: NotificationCreateResult): NotificationCommandResponse {
            return NotificationCommandResponse(
                groupId = result.groupId,
                senderId = result.senderId,
                notificationId = result.notificationId,
                state = result.status,
                type = result.type,
                createdAt = result.createdAt,
                scheduledAt = result.scheduledAt,
                dayOfWeeks = result.dayOfWeeks?.let{DayOfWeek.toList(it)},
                dayInterval = result.dayInterval,
                content = result.content
            )
        }
    }
}