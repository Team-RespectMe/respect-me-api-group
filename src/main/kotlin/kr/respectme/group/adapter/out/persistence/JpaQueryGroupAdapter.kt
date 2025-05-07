package kr.respectme.group.adapter.out.persistence

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.respectme.group.adapter.out.persistence.entity.GroupNotificationQueryModel
import kr.respectme.group.adapter.out.persistence.entity.NotificationGroupQueryModel
import kr.respectme.group.adapter.out.persistence.entity.QJpaGroupMember
import kr.respectme.group.adapter.out.persistence.entity.QJpaNotificationGroup
import kr.respectme.group.adapter.out.persistence.entity.notifications.*
import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.domain.GroupMemberRole
import kr.respectme.group.domain.GroupType
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.port.`in`.interfaces.vo.GroupMemberVo
import kr.respectme.group.port.`in`.interfaces.vo.NotificationGroupVo
import kr.respectme.group.port.`in`.interfaces.vo.Writer
import kr.respectme.group.port.out.persistence.QueryGroupPort
import org.aspectj.weaver.ast.Expr
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.util.*


@Repository
class JpaQueryGroupAdapter(
    private val qf: JPAQueryFactory
): QueryGroupPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun isPrivateGroup(groupId: UUID): Boolean {
        val group = QJpaNotificationGroup.jpaNotificationGroup
        val fetch = qf.select(group.type)
            .from(group)
            .where(group.id.eq(groupId))
            .limit(1)
            .fetch()

        return if(fetch.isEmpty()) {
            false
        } else {
            fetch[0] == GroupType.GROUP_PRIVATE
        }
    }

    override fun getGroup(loginId: UUID, groupId: UUID): NotificationGroupQueryModel? {
        val group = QJpaNotificationGroup.jpaNotificationGroup
        val member = QJpaGroupMember.jpaGroupMember
        val owner = QJpaGroupMember("owner")
        val me = QJpaGroupMember("me")

        return qf.select(
            Projections.constructor(
                NotificationGroupQueryModel::class.java,
                group.id,
                group.type,
                group.name,
                group.description,
                Projections.constructor(
                    GroupMemberVo::class.java,
                    owner.memberId,
                    owner.nickname,
                    owner.groupId,
                    owner.profileImageUrl,
                    owner.createdAt,
                    owner.memberRole
                ),
                member.id.countDistinct().intValue(),
                group.createdAt,
                group.thumbnail,
                me.memberRole.coalesce(GroupMemberRole.GUEST)
            )
        )
            .from(group)
            .leftJoin(member).on(member.groupId.eq(group.id))
            .leftJoin(owner).on(owner.groupId.eq(group.id).and(owner.memberRole.eq(GroupMemberRole.OWNER)))
            .leftJoin(me).on(me.groupId.eq(group.id).and(me.memberId.eq(loginId)))
            .where(group.id.eq(groupId))
            .groupBy(
                group.id,
                owner.id,
                me.memberRole,
            )
            .fetchOne()
    }

    /**
     * 사용자가 가입한 그룹 목록을 조회
     * @param loginId 사용자 ID
     * @return List<NotificationGroupDto>
     */
    override fun getMemberGroups(loginId: UUID): List<NotificationGroupQueryModel> {
        logger.error("getMemberGroups loginId: $loginId")
        val group = QJpaNotificationGroup.jpaNotificationGroup
        val member = QJpaGroupMember.jpaGroupMember
        val owner = QJpaGroupMember("owner")
        val me = QJpaGroupMember("me")

        val groupList = qf.select(member.groupId)
            .from(member)
            .where(member.memberId.eq(loginId))
            .fetch()

        return qf.select(
            Projections.constructor(
                NotificationGroupQueryModel::class.java,
                group.id,
                group.type,
                group.name,
                group.description,
                Projections.constructor(
                    GroupMemberVo::class.java,
                    owner.memberId,
                    owner.nickname,
                    owner.groupId,
                    owner.profileImageUrl,
                    owner.createdAt,
                    owner.memberRole
                ),
                member.id.countDistinct().intValue(),
                group.createdAt,
                group.thumbnail,
                me.memberRole.coalesce(GroupMemberRole.GUEST)
            )
        )
            .from(group)
            .leftJoin(member).on(member.groupId.eq(group.id))
            .leftJoin(owner).on(owner.groupId.eq(group.id).and(owner.memberRole.eq(GroupMemberRole.OWNER)))
            .leftJoin(me).on(me.groupId.eq(group.id).and(me.memberId.eq(loginId)))
            .where(group.id.`in`(groupList))
            .groupBy(
                group.id,
                owner.id,
                me.memberRole,
                me.createdAt
            )
            .orderBy(me.createdAt.desc())
            .fetch()
    }

    override fun getAllGroups(cursor: UUID?, size: Int?): List<NotificationGroupQueryModel> {
        val group = QJpaNotificationGroup.jpaNotificationGroup
        val member = QJpaGroupMember.jpaGroupMember
        val owner = QJpaGroupMember("owner")
        val me = QJpaGroupMember("me")

        return qf.select(
            Projections.constructor(
                NotificationGroupQueryModel::class.java,
                group.id,
                group.type,
                group.name,
                group.description,
                Projections.constructor(
                    GroupMemberVo::class.java,
                    owner.memberId,
                    owner.nickname,
                    owner.groupId,
                    owner.profileImageUrl,
                    owner.createdAt,
                    owner.memberRole
                ),
                member.id.countDistinct().intValue(),
                group.createdAt,
                group.thumbnail,
                Expressions.asEnum(GroupMemberRole.GUEST)
            )
        )
            .from(group)
            .leftJoin(member).on(member.groupId.eq(group.id))
            .leftJoin(owner).on(owner.groupId.eq(group.id).and(owner.memberRole.eq(GroupMemberRole.OWNER)))
            .where(lessOrEqualGroupId(cursor))
            .limit(size?.toLong() ?: 21)
            .groupBy(
                group.id,
                owner.id,
            )
            .orderBy(group.id.desc())
            .fetch()
    }

    override fun getGroupsByNameContainsKeyword(
        keyword: String,
        cursor: UUID?,
        size: Int
    ): List<NotificationGroupQueryModel> {
        val group = QJpaNotificationGroup.jpaNotificationGroup
        val member = QJpaGroupMember.jpaGroupMember
        val owner = QJpaGroupMember("owner")

        return qf.select(
            Projections.constructor(
                NotificationGroupQueryModel::class.java,
                group.id,
                group.type,
                group.name,
                group.description,
                Projections.constructor(
                    GroupMemberVo::class.java,
                    owner.memberId,
                    owner.nickname,
                    owner.groupId,
                    owner.profileImageUrl,
                    owner.createdAt,
                    owner.memberRole
                ),
                member.id.countDistinct().intValue(),
                group.createdAt,
                group.thumbnail,
                Expressions.asEnum(GroupMemberRole.GUEST)
            ))
            .from(group)
            .leftJoin(member).on(member.groupId.eq(group.id))
            .leftJoin(owner).on(owner.groupId.eq(group.id).and(owner.memberRole.eq(GroupMemberRole.OWNER)))
            .where(group.name.contains(keyword))
            .limit(size.toLong())
            .groupBy(
                group.id,
                owner.id,
            )
            .orderBy(group.id.desc())
            .fetch()
    }

    private fun lessOrEqualGroupId(cursor: UUID?): BooleanExpression {
        val group = QJpaNotificationGroup.jpaNotificationGroup
        return if (cursor != null) {
            group.id.loe(cursor)
        } else {
            Expressions.TRUE
        }
    }
}