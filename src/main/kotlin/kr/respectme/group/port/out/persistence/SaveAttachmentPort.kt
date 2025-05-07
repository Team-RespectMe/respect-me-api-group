package kr.respectme.group.port.out.persistence

import kr.respectme.group.domain.attachment.Attachment
import java.util.UUID

interface SaveAttachmentPort {

    fun persist(attachedFile: Attachment): Attachment

    fun update(attachedFile: Attachment): Attachment

    fun delete(attachedFile: Attachment): Boolean

    fun deleteById(id: UUID): Boolean
}