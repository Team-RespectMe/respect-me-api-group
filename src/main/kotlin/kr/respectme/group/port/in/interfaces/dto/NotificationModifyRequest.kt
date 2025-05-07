package kr.respectme.group.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import kr.respectme.group.domain.notifications.DayOfWeek
import kr.respectme.group.domain.notifications.NotificationType
import java.time.Instant


@Schema(description = "알림 생성 요청 객체")
data class NotificationModifyRequest(
    @Schema(required = true, example = "Notification Content", description = "알람 본문")
    @field: NotBlank(message = "Notification content must not be null or empty.")
    val content: String,
) {
}