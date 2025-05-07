package kr.respectme.group.port.`in`.interfaces

import kr.respectme.group.port.`in`.interfaces.dto.AttachmentRequest
import kr.respectme.group.port.`in`.interfaces.dto.AttachmentResponse
import java.util.*

interface AttachmentPort {

    fun getAttachments(loginId: UUID,
                       groupId: UUID,
                       notificationId: UUID): List<AttachmentResponse>

    fun linkAttachment(loginId: UUID,
                       groupId: UUID,
                       notificationId: UUID,
                       request: AttachmentRequest): AttachmentResponse

    fun unlinkAttachment(loginId: UUID,
                         groupId: UUID,
                         notificationId: UUID,
                         attachmentId: UUID)
}