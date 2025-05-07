package kr.respectme.group.application.command.useCase

import kr.respectme.group.application.dto.group.GroupCreateCommand
import kr.respectme.group.application.dto.group.GroupModifyCommand
import kr.respectme.group.application.dto.group.NotificationGroupDto
import java.util.UUID

interface NotificationGroupCommandUseCase {

    fun createNotificationGroup(loginId: UUID, command: GroupCreateCommand): NotificationGroupDto

    fun updateNotificationGroup(loginId: UUID, groupId: UUID, command: GroupModifyCommand): NotificationGroupDto

//    fun addMember(loginId: UUID, groupId: UUID, command: GroupMemberCreateCommand): GroupMemberDto

//    fun removeMember(loginId: UUID, groupId: UUID, memberIdToRemove: UUID): Unit

    fun deleteNotificationGroup(loginId: UUID, groupId: UUID): Unit

//    fun createNotification(loginId: UUID, groupId: UUID, command: NotificationCreateCommand): NotificationCreateResult

//    fun modifyNotificationContents(loginId: UUID, groupId: UUID, command: NotificationModifyCommand): NotificationCreateResult

//    fun modifyNotificationType(loginId: UUID, groupId: UUID, notificationId: UUID, command: NotificationModifyCommand): NotificationCreateResult
}