package kr.respectme.group.port.`in`.interfaces

import kr.respectme.group.application.dto.notification.NotificationCreateResult
import kr.respectme.group.port.`in`.interfaces.dto.NotificationCommandResponse
import kr.respectme.group.port.`in`.interfaces.dto.NotificationCreateRequest
import kr.respectme.group.port.`in`.interfaces.dto.NotificationModifyRequest
import java.util.*

interface NotificationCommandPort {

    fun createNotification(loginId: UUID,
                           groupId: UUID,
                           request: NotificationCreateRequest
    ): NotificationCommandResponse


    fun updateNotificationContent(loginId: UUID,
                           groupId: UUID,
                           notificationId: UUID,
                           request: NotificationModifyRequest
    ): NotificationCommandResponse

    fun updateNotification(loginId: UUID,
                           groupId: UUID,
                           notificationId: UUID,
                           request: NotificationModifyRequest)
    : NotificationCommandResponse

    fun deleteNotification(loginId: UUID,
                           groupId: UUID,
                           notificationId: UUID)
}