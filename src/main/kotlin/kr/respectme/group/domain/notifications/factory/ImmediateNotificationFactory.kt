package kr.respectme.group.domain.notifications.factory
//
//import kr.respectme.common.utility.UUIDV7Generator
//import kr.respectme.group.application.dto.notification.NotificationCreateCommand
//import kr.respectme.group.domain.notifications.ImmediateNotification
//import kr.respectme.group.domain.notifications.Notification

//object ImmediateNotificationFactory : NotificationFactory {
//
//    override fun build(command: NotificationCreateCommand): Notification {
//        return ImmediateNotification(
//            id = UUIDV7Generator.generate(),
//            groupId = command.groupId,
//            senderId = command.memberId,
//            content = command.content,
//        )
//    }
//}