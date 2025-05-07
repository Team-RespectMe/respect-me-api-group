package kr.respectme.group.adapter.out.persistence

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.respectme.group.adapter.out.persistence.entity.notifications.QJpaAttachment
import kr.respectme.group.domain.mapper.AttachmentMapper
import kr.respectme.group.domain.attachment.Attachment
import kr.respectme.group.port.out.persistence.LoadAttachmentPort
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class JpaLoadAttachmentAdapter(
    private val qf: JPAQueryFactory,
    private val mapper: AttachmentMapper
) : LoadAttachmentPort {

    override fun loadById(id: UUID): Attachment? {
        val jpaAttachment = QJpaAttachment.jpaAttachment
        val jpaEntity = qf.selectFrom(jpaAttachment)
            .where(jpaAttachment.id.eq(id))
            .fetchOne()
        return jpaEntity?.let { mapper.toDomain(jpaEntity) }
    }

    override fun loadByResourceIdAndNotificationId(resourceId: UUID, notificationId: UUID): Attachment? {
        val jpaAttachment = QJpaAttachment.jpaAttachment

        val jpaEntity = qf.selectFrom(jpaAttachment)
            .where(
                jpaAttachment.resourceId.eq(resourceId),
                jpaAttachment.notificationId.eq(notificationId)
            )
            .fetchOne()

        return jpaEntity?.let { mapper.toDomain(it) }
    }

    override fun loadByNotificationId(notificationId: UUID): List<Attachment> {
        val jpaAttachment = QJpaAttachment.jpaAttachment
        val jpaEntities = qf.selectFrom(jpaAttachment)
            .where(jpaAttachment.notificationId.eq(notificationId))
            .fetch()
        return jpaEntities.map { mapper.toDomain(it) }
    }

    override fun findByNotificationId(notificationIds: List<UUID>): List<Attachment> {
        val jpaAttachment = QJpaAttachment.jpaAttachment
        val jpaEntities = qf.selectFrom(jpaAttachment)
            .where(jpaAttachment.notificationId.`in`(notificationIds))
            .fetch()
        return jpaEntities.map { mapper.toDomain(it) }
    }

    override fun findByResourceIds(ids: List<UUID>): List<Attachment> {
        val jpaAttachment = QJpaAttachment.jpaAttachment

        return qf.select(jpaAttachment)
            .from(jpaAttachment)
            .where(jpaAttachment.id.`in`(ids))
            .fetch()
            .map { mapper.toDomain(it) }
    }
}