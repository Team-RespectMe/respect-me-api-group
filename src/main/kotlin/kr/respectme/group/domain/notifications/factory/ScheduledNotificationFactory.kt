package kr.respectme.group.domain.notifications.factory

//import kr.respectme.common.error.BadRequestException
//import kr.respectme.common.utility.UUIDV7Generator
//import kr.respectme.group.application.dto.notification.NotificationCreateCommand
//import kr.respectme.group.common.errors.GroupServiceErrorCode.GROUP_NOTIFICATION_RESERVED_AT_INVALID
//import kr.respectme.group.domain.notifications.Notification
//import kr.respectme.group.domain.notifications.ScheduledNotification
//
//object ScheduledNotificationFactory: NotificationFactory {
//
//    override fun build(command: NotificationCreateCommand): Notification {
//        return ScheduledNotification(
//            id = UUIDV7Generator.generate(),
//            groupId = command.groupId,
//            senderId = command.memberId,
//            content = command.content,
//            scheduledAt = command.scheduledAt
//                ?: throw BadRequestException(GROUP_NOTIFICATION_RESERVED_AT_INVALID),
//        )
//    }
//}