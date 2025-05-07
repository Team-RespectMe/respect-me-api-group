package kr.respectme.group.domain.attachment

import kr.respectme.common.domain.annotations.DomainEntity
import kr.respectme.group.domain.notifications.Notification
import kr.respectme.group.port.out.persistence.LoadAttachmentPort
import kr.respectme.group.port.out.persistence.SaveAttachmentPort
import java.util.UUID

@DomainEntity
class Attachment(
    val id: UUID? = null,
    val type: AttachmentType,
    val groupId: UUID,
    val notificationId: UUID,
    val resourceId: UUID,
) {

    val identifier: UUID
        get() = id ?: throw IllegalStateException("Attached file ID is not set")
}