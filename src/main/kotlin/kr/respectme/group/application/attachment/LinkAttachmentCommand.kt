package kr.respectme.group.application.attachment

import kr.respectme.group.domain.attachment.AttachmentType
import kr.respectme.group.port.`in`.interfaces.dto.AttachmentRequest
import java.util.UUID

class LinkAttachmentCommand(
    val type: AttachmentType,
    val resourceId: UUID,
) {

    companion object {

        fun of(groupId: UUID,
               notificationId: UUID,
               request: AttachmentRequest): LinkAttachmentCommand {
            return LinkAttachmentCommand(
                type = request.type,
                resourceId = request.resourceId,
            )
        }
    }
}