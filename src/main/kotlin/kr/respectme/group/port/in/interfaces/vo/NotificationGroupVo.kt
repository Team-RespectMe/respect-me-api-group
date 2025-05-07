package kr.respectme.group.port.`in`.interfaces.vo

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

/**
 * 그룹의 기본 정보만을 표시하는 Vo
 */
@Schema(description = "그룹 간략 정보")
data class NotificationGroupVo(
    @Schema(description = "그룹 ID")
    val id: UUID = UUID.randomUUID(),
    @Schema(description = "그룹 이름")
    val name: String = "",
    @Schema(description = "그룹 이미지 URL")
    val thumbnail: String? = null
) {
}