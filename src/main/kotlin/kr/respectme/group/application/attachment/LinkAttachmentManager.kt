package kr.respectme.group.application.attachment

import kr.respectme.common.error.BadRequestException
import kr.respectme.common.error.ForbiddenException
import kr.respectme.common.error.NotFoundException
import kr.respectme.common.error.UnsupportedMediaTypeException
import kr.respectme.group.common.errors.GroupServiceErrorCode
import kr.respectme.group.domain.attachment.Attachment
import kr.respectme.group.port.out.persistence.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class LinkAttachmentManager(
    private val handlers: List<AttachmentHandler>,
    private val notificationPort: LoadNotificationPort,
    private val loadMemberPort: LoadMemberPort,
    private val loadAttachmentPort: LoadAttachmentPort,
    private val saveAttachmentPort: SaveAttachmentPort
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun getAttachments(loginId: UUID, groupId: UUID, notificationId: UUID): List<AttachmentDto> {
        val notification = notificationPort.loadEntityById(notificationId)
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_NOTIFICATION_NOT_EXISTS)
        val member = loadMemberPort.load(groupId, loginId)
            ?: throw ForbiddenException(GroupServiceErrorCode.GROUP_MEMBER_NOT_ENOUGH_PERMISSION)

        return loadAttachmentPort.loadByNotificationId(notificationId)
            .map { AttachmentDto.of(it) }
    }

    @Transactional
    fun link(loginId: UUID, commands: List<LinkAttachmentCommand>)
            : List<AttachmentDto> {
        val existsAttachments = loadAttachmentPort.loadByNotificationId(
            commands.first().notificationId
        )

        val deletedAttachments = existsAttachments.filter { attachment ->
            commands.none { command -> command.resourceId == attachment.resourceId }
        }

        val newAttachmentCommands = commands.filter { command ->
            existsAttachments.none { attachment -> attachment.resourceId == command.resourceId }
        }

        deletedAttachments.forEach { attachment ->
            saveAttachmentPort.delete(attachment)
        }

        return newAttachmentCommands.map { command ->
            handlers.find { handler -> handler.isSupport(command) }
                ?.linkAttachment(loginId, command = command)
                ?: throw UnsupportedMediaTypeException(GroupServiceErrorCode.GROUP_NOTIFICATION_NOT_SUPPORTED_ATTACHMENT_TYPE)
        }
    }

    @Transactional
    fun unlink(loginId: UUID, notificationId: UUID, attachmentId: UUID) {
        val notification = notificationPort.loadEntityById(notificationId)
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_NOTIFICATION_NOT_EXISTS)

        if (notification.getSenderId() != loginId) {
            throw ForbiddenException(GroupServiceErrorCode.GROUP_NOTIFICATION_SENDER_MISMATCH)
        }

        val attachment = loadAttachmentPort.loadById(attachmentId)
            ?: return;

        if ( attachment.notificationId != notificationId ) {
            throw BadRequestException(GroupServiceErrorCode.GROUP_NOTIFICATION_ATTACHMENT_NOTIFICATION_ID_MISMATCH)
        }

        saveAttachmentPort.delete(attachment)
    }
}