package kr.respectme.group.domain.mapper

import kr.respectme.group.adapter.out.persistence.entity.notifications.JpaAttachment
import kr.respectme.group.domain.attachment.Attachment
import org.springframework.stereotype.Component

@Component
class AttachmentMapper {

    fun toDomain(jpaEntity: JpaAttachment): Attachment {
        return Attachment(
            id = jpaEntity.identifier,
            resourceId = jpaEntity.resourceId,
            type = jpaEntity.type,
            groupId = jpaEntity.groupId,
            notificationId = jpaEntity.notificationId
        )
    }

    fun toJpaEntity(domain: Attachment): JpaAttachment {
        return JpaAttachment(
            id = domain.id,
            type = domain.type,
            resourceId = domain.resourceId,
            groupId = domain.groupId,
            notificationId = domain.notificationId
        )
    }
}