package kr.respectme.group.adapter.out.persistence

import kr.respectme.common.domain.cache.DomainEntityCache
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupMemberRepository
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupNotificationRepository
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupRepository
import kr.respectme.group.domain.GroupMember
import kr.respectme.group.domain.NotificationGroup
import kr.respectme.group.domain.mapper.GroupMapper
import kr.respectme.group.port.out.persistence.LoadGroupPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class JpaLoadGroupAdapter(
    private val entityCache: DomainEntityCache,
    private val groupRepository: JpaGroupRepository,
    private val groupMemberRepository: JpaGroupMemberRepository,
    private val groupNotificationRepository: JpaGroupNotificationRepository,
    private val groupMapper: GroupMapper,
): LoadGroupPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun loadGroup(groupId: UUID): NotificationGroup? {
        val group = groupRepository.findById(groupId)

        if (group == null) {
            logger.warn("Group not found: $groupId")
            return null
        }

        val owner = groupMemberRepository.findByGroupIdAndMemberId(groupId, group.ownerId)

        if(owner == null) {
            logger.warn("Group members not found: $groupId")
            return null
        }

        val domainGroup = groupMapper.toDomain(group, owner)
        entityCache.put(NotificationGroup::class.java, domainGroup)
        entityCache.put(GroupMember::class.java, domainGroup.getOwner())

        return domainGroup
    }

    override fun loadGroupMemberIds(groupId: UUID): List<UUID> {
        return groupMemberRepository.findByGroupId(groupId)
            .map { it.memberId }
    }

    private fun cacheGroupMembers(members: List<GroupMember>) {
        members.forEach { member ->
            entityCache.put(GroupMember::class.java, member)
        }
    }
}