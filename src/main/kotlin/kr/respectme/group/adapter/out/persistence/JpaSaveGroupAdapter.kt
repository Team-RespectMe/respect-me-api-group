package kr.respectme.group.adapter.out.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.common.domain.cache.DomainEntityCache
import kr.respectme.common.domain.enums.EntityStatus
import kr.respectme.common.error.BadRequestException
import kr.respectme.common.error.BusinessException
import kr.respectme.common.error.InternalServerError
import kr.respectme.group.adapter.out.persistence.entity.JpaGroupMember
import kr.respectme.group.adapter.out.persistence.entity.JpaNotificationGroup
import kr.respectme.group.adapter.out.persistence.entity.notifications.JpaGroupNotification
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupMemberRepository
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupNotificationRepository
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupRepository
import kr.respectme.group.domain.GroupMember
import kr.respectme.group.domain.NotificationGroup
import kr.respectme.group.domain.mapper.GroupMapper
import kr.respectme.group.domain.mapper.GroupMemberMapper
import kr.respectme.group.domain.mapper.NotificationMapper
import kr.respectme.group.domain.notifications.*
import kr.respectme.group.port.out.persistence.SaveGroupPort
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.util.*
import javax.swing.text.html.parser.Entity


@Component
class JpaSaveGroupAdapter(
    private val entityCache: DomainEntityCache,
    private val groupMapper: GroupMapper,
    private val memberMapper: GroupMemberMapper,
    private val groupRepository: JpaGroupRepository,
    private val groupMemberRepository: JpaGroupMemberRepository,
) : SaveGroupPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun save(group: NotificationGroup): NotificationGroup {
        val groupEntity = groupMapper.toEntity(group)
        val owner = memberMapper.toEntity(group.getOwner())

        if(entityCache.isSameWithCache(NotificationGroup::class.java, group) == EntityStatus.ACTIVE) {
            return group
        }

        groupRepository.save(groupEntity)
        groupMemberRepository.save(owner)
        return groupMapper.toDomain(groupEntity, owner)
    }

    override fun deleteById(id: UUID) {
        groupRepository.deleteById(id)
    }

    override fun delete(group: NotificationGroup) {
        deleteById(group.id)
    }
}