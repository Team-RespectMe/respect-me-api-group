package kr.respectme.group.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.annotation.Nullable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import kr.respectme.group.application.attachment.AttachmentDto
import kr.respectme.group.domain.attachment.Attachment
import kr.respectme.group.domain.notifications.DayOfWeek
import kr.respectme.group.domain.notifications.NotificationType
import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant
import java.time.OffsetTime
import java.util.UUID

@Schema(description = "알림 생성 요청 객체")
data class NotificationCreateRequest(
    @Schema(required = true, example = "Notification Content", description = "알람 본문")
    @field: NotBlank(message = "Notification content must not be null or empty.")
    val content: String,
    @Schema(required = true, example = "IMMEDIATE", description = "알람 타입, IMMEDIATE: 즉시 <br/>" +
            "SCHEDULED: 예약 <br/>" +
            "REPEATED_WEEKLY: 매주 특정 요일 <br/>" +
            "REPEATED_DAY_INTERVAL: 매일 일정 간격 <br/>")
    @field: NotNull(message = "Notification type must not be null or empty.")
    val type: NotificationType,
    @Schema(description = "알림에 첨부될 리소스", example = """{
            "type" : "FILE",
            "resourceId": "791763ca-5da8-4aca-9c94-aa9d22a5fd10",
        }
    """)
    val attachments: List<AttachmentRequest> = emptyList()

//    @Schema(required = false, example = "2021-08-01T00:00:00Z", description = "예약 알람 시간, 타입이 SCHEDULED 일 때 필수")
//    val scheduledAt: Instant? = null,
//    @Schema(required = false, example = "12:00:00Z", description = "반복 알람 시간, 타입이 REPEATED_WEEKLY 또는 REPEATED_INTERVAL 일 때 필수")
//    val triggerTime: OffsetTime? = null,
//    @Schema(required = false, example = "[MONDAY, TUESDAY]", description = "반복 알람 요일, 타입이 REPEATED_WEEKLY 일 때 필수")
//    val dayOfWeeks: List<DayOfWeek>? = null,
//    @Schema(required = false, example = "1", description = "반복 알람 일 간격, 타입이 REPEATED_DAY_INTERVAL 일 때 필수")
//    val dayInterval: Int? = null
) {
}