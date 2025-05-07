package kr.respectme.group.port.`in`.interfaces

import kr.respectme.group.port.`in`.interfaces.dto.*
import java.util.UUID

interface NotificationGroupCommandPort {

    fun createNotificationGroup(loginId: UUID, request: GroupCreateRequest): NotificationGroupResponse

    fun updateNotificationGroup(loginId: UUID, groupId: UUID, request: GroupModifyRequest): NotificationGroupResponse

    fun deleteNotificationGroup(loginId: UUID,groupID: UUID)
}