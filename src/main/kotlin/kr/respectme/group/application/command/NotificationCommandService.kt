package kr.respectme.group.application.command

import kr.respectme.common.error.ForbiddenException
import kr.respectme.common.error.NotFoundException
//import kr.respectme.group.application.AttachedFileService
import kr.respectme.group.application.command.useCase.NotificationCommandUseCase
import kr.respectme.group.application.dto.notification.NotificationCreateCommand
import kr.respectme.group.application.dto.notification.NotificationCreateResult
import kr.respectme.group.application.dto.notification.NotificationModifyCommand
import kr.respectme.group.common.errors.GroupServiceErrorCode
import kr.respectme.group.domain.notifications.Notification
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.port.out.event.EventPublishPort
import kr.respectme.group.port.out.event.dto.NotificationCreateEvent
import kr.respectme.group.port.out.persistence.LoadGroupPort
import kr.respectme.group.port.out.persistence.LoadMemberPort
import kr.respectme.group.port.out.persistence.LoadNotificationPort
import kr.respectme.group.port.out.persistence.SaveNotificationPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class NotificationCommandService(
    private val loadGroupPort: LoadGroupPort,
    private val loadMemberPort: LoadMemberPort,
    private val saveNotificationPort: SaveNotificationPort,
    private val loadNotificationPort: LoadNotificationPort,
//    private val attachedFileService: AttachedFileService,
    private val eventPublishPort: EventPublishPort
)
: NotificationCommandUseCase {

    private val logger = LoggerFactory.getLogger(javaClass)

    private final val MAX_NOTIFICATION_COUNT_PER_DAY = 5

    @Transactional
    override fun createNotification(
        loginId: UUID,
        groupId: UUID,
        command: NotificationCreateCommand
    ): NotificationCreateResult {
        val group = loadGroupPort.loadGroup(groupId)
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_NOT_FOUND)
        val member = loadMemberPort.load(groupId, loginId)
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_MEMBER_NOT_FOUND)

        logger.info("[NotificationCreateEvent] group create request member : ${loginId}\n command : $command")

        if ( member.isGroupMember() ) {
            logger.info("[NotificationCreateEvent] group create request member : ${loginId} is not group member")
            throw ForbiddenException(GroupServiceErrorCode.GROUP_MEMBER_NOT_ENOUGH_PERMISSION)
        }

        val leftNotificationCount = loadNotificationPort.countTodayGroupNotification(groupId)

        if ( leftNotificationCount > MAX_NOTIFICATION_COUNT_PER_DAY ) {
            logger.info("[NotificationCreateEvent] group ${groupId} exceed notification limit")
            throw ForbiddenException(GroupServiceErrorCode.GROUP_NOTIFICATION_EXCEED_LIMIT)
        }

        val notification = Notification(
            groupId = groupId,
            senderId = loginId,
            content = command.content,
            type = command.type,
            status = NotificationStatus.PUBLISHED
        )

        saveNotificationPort.saveNotification(notification)
        val receivers = loadMemberPort.findMemberIdsByGroupId(groupId)
            .filter { it != notification.getSenderId() }
        val event = NotificationCreateEvent.valueOf(group, notification, receivers)
        logger.info("[NotificationCreateEvent] new notification created : ${notification.id} group name: ${group.getName()} sender: ${notification.getSenderId()} content: ${notification.getContent()}")

//        attachedFileService.batchUpdate(notification.id, command.fileIds)
        eventPublishPort.publish(NotificationCreateEvent.eventName, event)

        return NotificationCreateResult.valueOf(notification)
    }

    @Transactional
    override fun updateNotification(
        loginId: UUID,
        groupId: UUID,
        notificationId: UUID,
        command: NotificationModifyCommand
    ): NotificationCreateResult {
        var notification = loadNotificationPort.loadEntityById(notificationId)
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_NOTIFICATION_NOT_EXISTS)

        if ( notification.getGroupId() != groupId ) {
            logger.info("[NotificationUpdateEvent] group update request rejected, member : ${loginId} is not group owner. owner Id ${notification.getGroupId()}")
            throw ForbiddenException(GroupServiceErrorCode.GROUP_NOTIFICATION_GROUP_ID_MISMATCH)
        }

        if ( notification.getSenderId() != loginId ) {
            logger.info("[NotificationUpdateEvent] group update request rejected, member : ${loginId} is not group owner. owner Id ${notification.getSenderId()}")
            throw ForbiddenException(GroupServiceErrorCode.GROUP_NOTIFICATION_MEMBER_ID_MISTMATCH)
        }
        logger.info("[NotificationUpdateEvent] group update request member : ${loginId}\n command : $command")

        notification.updateContent(command.content)
        notification = saveNotificationPort.saveNotification(notification)
        logger.info("[NotificationUpdateEvent] notification update completed : ${loginId}\n command : $command")

        return NotificationCreateResult.valueOf(notification)
    }

    @Transactional
    override fun deleteNotification(loginId: UUID, groupId: UUID, notificationId: UUID) {
        val notification = loadNotificationPort.loadEntityById(notificationId)
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_NOTIFICATION_NOT_EXISTS)

        logger.info("[NotificationDeleteEvent] group delete request member : ${loginId} notification id : ${notificationId}")

        if ( notification.getGroupId() != groupId ) {
            logger.info("[NotificationDeleteEvent] group delete request rejected, member : ${loginId} is not group owner. owner Id ${notification.getGroupId()}")
            throw ForbiddenException(GroupServiceErrorCode.GROUP_NOTIFICATION_GROUP_ID_MISMATCH)
        }

        val member = loadMemberPort.load(groupId, loginId)
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_MEMBER_NOT_FOUND)

        if ( member.isGroupMember() ) {
            logger.info("[NotificationDeleteEvent] group delete request rejected, member : ${loginId} is not group owner. owner Id ${notification.getGroupId()}")
            throw ForbiddenException(GroupServiceErrorCode.GROUP_MEMBER_NOT_ENOUGH_PERMISSION)
        }

        logger.info("[NotificationDeleteEvent] group delete request accepted, member : ${loginId} deleted notification ${notificationId} group name : ${notification.getGroupId()}.")

        saveNotificationPort.deleteNotification(notification)
    }
}