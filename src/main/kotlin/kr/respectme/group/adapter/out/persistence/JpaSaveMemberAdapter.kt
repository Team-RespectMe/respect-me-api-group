package kr.respectme.group.adapter.out.persistence

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import kr.respectme.common.domain.cache.DomainEntityCache
import kr.respectme.group.adapter.out.persistence.entity.JpaGroupMember
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupMemberRepository
import kr.respectme.group.domain.GroupMember
import kr.respectme.group.domain.mapper.GroupMemberMapper
import kr.respectme.group.port.out.persistence.SaveMemberPort
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JpaSaveMemberAdapter(
    @PersistenceContext
    private val em: EntityManager,
    private val memberRepository: JpaGroupMemberRepository,
    private val domainEntityCache: DomainEntityCache,
    private val memberMapper: GroupMemberMapper
): SaveMemberPort {

    override fun save(entity: GroupMember): GroupMember {
        var jpaEntity = memberMapper.toEntity(entity)

        if(domainEntityCache.contains(GroupMember::class.java, entity.id)) {
            jpaEntity = em.merge(jpaEntity)
        } else {
            em.persist(jpaEntity)
        }

        return memberMapper.toDomain(jpaEntity)
    }

    override fun delete(entity: GroupMember) {
        val entity = em.find(JpaGroupMember::class.java, entity.id)
        em.remove(entity)
    }

    override fun delete(groupId: UUID, memberId: UUID) {
        val groupMembers = memberRepository.findByMemberIdInAndGroupId(listOf(memberId), groupId)
        groupMembers.forEach { memberRepository.delete(it) }
    }
}