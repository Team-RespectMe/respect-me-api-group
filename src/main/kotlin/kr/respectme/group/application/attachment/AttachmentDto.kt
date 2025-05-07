package kr.respectme.group.application.attachment

import kr.respectme.group.domain.attachment.Attachment
import kr.respectme.group.domain.attachment.AttachmentType
import java.util.UUID

data class AttachmentDto(
    val id: UUID,
    val type: AttachmentType,
    val groupId: UUID,
    val notificationId: UUID,
    val resourceId: UUID ,
) {

    companion object {
        fun of(entity: Attachment): AttachmentDto {
            return AttachmentDto(
                id = entity.identifier,
                type = entity.type,
                resourceId = entity.resourceId,
                notificationId = entity.notificationId,
                groupId = entity.groupId
            )
        }
    }
}