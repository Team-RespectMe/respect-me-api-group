package kr.respectme.group.adapter.out.persistence.entity.notifications

import jakarta.persistence.*
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.group.domain.attachment.AttachmentConverter
import kr.respectme.group.domain.attachment.AttachmentType
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.UUID

@Entity(name = "attachment")
@EntityListeners(AuditingEntityListener::class)
class JpaAttachment(
    @Id
    private var id: UUID? = null,
    @Column
    val resourceId: UUID = UUID.randomUUID(),
    @Convert(converter = AttachmentConverter::class)
    val type: AttachmentType = AttachmentType.FILE,
    @Column
    val notificationId: UUID = UUID.randomUUID(),
    @Column
    val groupId: UUID = UUID.randomUUID()
) {

    fun getId(): UUID? = id

    val identifier: UUID
        get() = id ?: throw IllegalStateException("Attachment ID is not set")

    @PrePersist
    fun prePersist() {
        if (id == null) {
            id = UUIDV7Generator.generate()
        }
    }
}