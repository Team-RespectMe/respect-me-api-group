package kr.respectme.group.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.respectme.group.application.dto.group.NotificationGroupDto
import java.util.*

@Schema(name = "NotificationGroupResponse", description = "알림 그룹 커맨드 요청에 대한 응답 객체")
data class NotificationGroupResponse(
    val groupId: UUID,
    val groupName: String,
    val ownerId: UUID,
    val groupDescription: String,
    val groupThumbnail: String? = null,
    val groupType: String,
) {
    companion object {

        fun valueOf(
            groupDto: NotificationGroupDto
        ): NotificationGroupResponse {
            return NotificationGroupResponse(
                groupId = groupDto.id,
                groupName = groupDto.name,
                ownerId = groupDto.ownerId,
                groupDescription = groupDto.description,
                groupThumbnail = groupDto.imageUrl,
                groupType = groupDto.groupType.name,
            )
        }
    }
}