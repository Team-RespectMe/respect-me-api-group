package kr.respectme.group.application.command.useCase

import kr.respectme.group.application.dto.notification.NotificationCreateCommand
import kr.respectme.group.application.dto.notification.NotificationCreateResult
import kr.respectme.group.application.dto.notification.NotificationModifyCommand
import kr.respectme.group.port.`in`.interfaces.dto.NotificationCommandResponse
import java.util.UUID

interface NotificationCommandUseCase {

    fun createNotification(loginId: UUID,
                           groupId: UUID,
                           command: NotificationCreateCommand)
    : NotificationCreateResult

    fun updateNotification(loginId: UUID,
                           groupId: UUID,
                           notificationId: UUID,
                           command: NotificationModifyCommand)
    : NotificationCreateResult

    fun deleteNotification(loginId: UUID, groupId: UUID, notificationId: UUID)
}