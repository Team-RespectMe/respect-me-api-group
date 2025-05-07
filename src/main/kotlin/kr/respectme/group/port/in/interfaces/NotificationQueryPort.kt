package kr.respectme.group.port.`in`.interfaces

import kr.respectme.group.adapter.out.persistence.entity.GroupNotificationQueryModel
import kr.respectme.group.application.dto.notification.NotificationCountDto
import kr.respectme.group.port.`in`.interfaces.dto.GroupNotificationQueryResponse
import java.util.*

interface NotificationQueryPort {

    fun getTodayLeftNotificationCount(loginId: UUID, groupId: UUID): NotificationCountDto

    fun getGroupNotifications(loginId: UUID, groupId: UUID, cursor: UUID?, size: Int?): List<GroupNotificationQueryResponse>

    fun getNotification(loginId: UUID, groupId: UUID, notificationId: UUID): GroupNotificationQueryResponse

    fun getMemberNotifications(loginId: UUID, cursor: UUID?, size: Int?): List<GroupNotificationQueryResponse>
}