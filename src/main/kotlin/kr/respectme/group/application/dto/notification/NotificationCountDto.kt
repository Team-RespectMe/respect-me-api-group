package kr.respectme.group.application.dto.notification

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(name = "NotificationCountDto", description = " 그룹의 남은 알림 개수 반환")
data class NotificationCountDto(
    @Schema(name = "groupId", description = "그룹 아이디")
    val groupId: UUID,
    @Schema(name = "count", description = "남은 알림 개수")
    val count: Int
) {

}