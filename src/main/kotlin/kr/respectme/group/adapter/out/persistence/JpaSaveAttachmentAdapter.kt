package kr.respectme.group.adapter.out.persistence

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import kr.respectme.group.adapter.out.persistence.entity.notifications.JpaAttachment
import kr.respectme.group.adapter.out.persistence.entity.notifications.QJpaAttachment
import kr.respectme.group.domain.mapper.AttachmentMapper
import kr.respectme.group.domain.attachment.Attachment
import kr.respectme.group.port.out.persistence.SaveAttachmentPort
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JpaSaveAttachmentAdapter(
    @PersistenceContext
    private val em: EntityManager,
    private val mapper: AttachmentMapper,
    private val qf: JPAQueryFactory
) : SaveAttachmentPort {

    override fun persist(attachedFile: Attachment): Attachment {
        val jpaEntity = mapper.toJpaEntity(attachedFile)

        if ( jpaEntity.getId() != null ) {
            throw IllegalStateException("Attached file ID is already set")
        }

        em.persist(jpaEntity)

        return mapper.toDomain(jpaEntity)
    }

    override fun update(attachedFile: Attachment): Attachment {
        var jpaEntity = mapper.toJpaEntity(attachedFile)

        if ( jpaEntity.getId() == null ) {
            throw IllegalStateException("Attached file ID is not set")
        }

        val jpaAttachment = QJpaAttachment.jpaAttachment
        val exist = qf.select(jpaAttachment.id)
            .from(jpaAttachment)
            .where(jpaAttachment.id.eq(jpaEntity.getId()))
            .fetchOne()

        if ( exist == null ) {
            throw IllegalStateException("Attached file not found: ${jpaEntity.getId()}")
        }

        jpaEntity = em.merge(jpaEntity)

        return mapper.toDomain(jpaEntity)
    }

    override fun delete(attachedFile: Attachment): Boolean {
        if ( attachedFile.id == null ) {
            return false
        }

        return deleteById(attachedFile.identifier)
    }

    override fun deleteById(id: UUID): Boolean {
        val jpaEntity = em.find(JpaAttachment::class.java, id)
        if (jpaEntity != null) {
            em.remove(jpaEntity)
        } else {
            return false
        }
        return true
    }
}