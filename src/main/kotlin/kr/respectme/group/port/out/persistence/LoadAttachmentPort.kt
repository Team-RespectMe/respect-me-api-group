package kr.respectme.group.port.out.persistence

import kr.respectme.group.domain.attachment.Attachment
import java.util.UUID

interface LoadAttachmentPort {

    fun loadById(id: UUID): Attachment?

    fun loadByResourceIdAndNotificationId(resourceId: UUID, notificationId: UUID): Attachment?

    fun loadByNotificationId(notificationId: UUID): List<Attachment>

    fun findByNotificationId(notificationIds: List<UUID>): List<Attachment>

    fun findByResourceIds(ids: List<UUID>): List<Attachment>
}