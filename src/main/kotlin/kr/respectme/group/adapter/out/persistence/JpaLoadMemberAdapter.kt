package kr.respectme.group.adapter.out.persistence

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.respectme.common.domain.cache.DomainEntityCache
import kr.respectme.group.adapter.out.persistence.entity.QJpaGroupMember
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupMemberRepository
import kr.respectme.group.domain.GroupMember
import kr.respectme.group.domain.mapper.GroupMemberMapper
import kr.respectme.group.port.out.persistence.LoadMemberPort
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JpaLoadMemberAdapter(
    private val qf: JPAQueryFactory,
    private val memberRepository: JpaGroupMemberRepository,
    private val memberMapper: GroupMemberMapper,
    private val domainEntityCache: DomainEntityCache
): LoadMemberPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun load(groupId: UUID, memberId: UUID): GroupMember? {
        val entity = memberRepository.findByGroupIdAndMemberId(groupId, memberId)

        if(entity == null) {
            return entity
        }

        val domainEntity = memberMapper.toDomain(entity)
        domainEntityCache.put(GroupMember::class.java, domainEntity)
        return domainEntity
    }

    override fun findMemberIdsByGroupId(groupId: UUID): List<UUID> {
        return memberRepository.findByGroupId(groupId)
            .map { it.memberId }
    }

    override fun findByMemberId(memberId: UUID): List<GroupMember> {
        return memberRepository.findAllByMemberId(memberId).map { memberMapper.toDomain(it) }
    }

    override fun findByGroupIdAndMemberId(groupId: UUID, memberId: UUID): GroupMember? {
        val groupMember = QJpaGroupMember.jpaGroupMember

        val entity = qf.select(groupMember)
            .from(groupMember)
            .where(groupMember.groupId.eq(groupId))
            .where(groupMember.memberId.eq(memberId))
            .fetchOne()

        if(entity == null) {
            return entity
        }

        return memberMapper.toDomain(entity)
    }

    override fun findAllByGroupId(groupId: UUID, cursor: UUID?, pageable: Pageable): Slice<GroupMember> {
        logger.debug("findAllByGroupId groupId : $groupId, cursor : $cursor, pageable : $pageable")
        val member = QJpaGroupMember.jpaGroupMember

        val entity = qf.select(member)
            .from(member)
            .where(member.groupId.eq(groupId).and(loeMemberId(cursor)))
            .orderBy(member.memberId.desc())
            .limit(pageable.pageSize.toLong())
            .fetch()

        logger.debug("findAllByGroupId result : ${entity.size}")

        return SliceImpl(
            entity.map{ memberMapper.toDomain(it) },
            pageable,
            entity.size == pageable.pageSize
        )
    }

    private fun loeMemberId(memberId: UUID?): BooleanExpression {
        val member = QJpaGroupMember.jpaGroupMember;

        return if (memberId != null) {
            member.memberId.loe(memberId)
        } else {
            Expressions.TRUE
        }
    }
}