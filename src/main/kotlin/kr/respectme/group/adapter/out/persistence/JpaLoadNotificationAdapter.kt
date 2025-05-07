package kr.respectme.group.adapter.out.persistence

import com.querydsl.core.types.ConstructorExpression
import com.querydsl.core.types.NullExpression
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.respectme.group.adapter.out.persistence.entity.GroupNotificationQueryModel
import kr.respectme.group.adapter.out.persistence.entity.QJpaGroupMember
import kr.respectme.group.adapter.out.persistence.entity.QJpaNotificationGroup
import kr.respectme.group.adapter.out.persistence.entity.notifications.QJpaGroupNotification
import kr.respectme.group.domain.mapper.NotificationMapper
import kr.respectme.group.domain.notifications.Notification
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.port.`in`.interfaces.vo.NotificationGroupVo
import kr.respectme.group.port.`in`.interfaces.vo.Writer
import kr.respectme.group.port.out.persistence.LoadNotificationPort
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.stereotype.Repository
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Repository
class JpaLoadNotificationAdapter(
    private val qf: JPAQueryFactory,
    private val notificationMapper: NotificationMapper
): LoadNotificationPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun loadEntityById(id: UUID): Notification? {
        val notification = QJpaGroupNotification.jpaGroupNotification
        val entity = qf.select(notification)
            .from(notification)
            .where(notification.id.eq(id))
            .fetchOne()

        if(entity == null) {
            return null
        }

        return notificationMapper.toDomain(entity)
    }

    override fun findNotificationById(notificationId: UUID)
    : GroupNotificationQueryModel? {
        val notification = QJpaGroupNotification.jpaGroupNotification
        val group = QJpaNotificationGroup.jpaNotificationGroup
        val member = QJpaGroupMember.jpaGroupMember

        return qf.select(
            Projections.constructor(
                GroupNotificationQueryModel::class.java,
                notification.id,
                notification.type,
                getGroupInfo(group),
                notification.status,
                getWriter(member),
                notification.content,
                NullExpression<Instant>(Instant::class.java),
                NullExpression<Int>(Int::class.java),
                NullExpression<Int>(Int::class.java),
                notification.createdAt,
                notification.updatedAt,
                notification.lastSentAt
            ))
            .from(notification)
            .join(group)
                .on(notification.groupId.eq(group.id))
            .join(member)
                .on(notification.memberId.eq(member.memberId).and(notification.groupId.eq(member.groupId)))
            .where(notification.id.eq(notificationId))
            .distinct()
            .fetchOne()
    }

    override fun findNotificationsByGroupId(
        groupId: UUID,
        cursorId: UUID?,
        pageable: Pageable
    ): Slice<GroupNotificationQueryModel> {
        val notification = QJpaGroupNotification.jpaGroupNotification
        val group = QJpaNotificationGroup.jpaNotificationGroup
        val member = QJpaGroupMember.jpaGroupMember

        val fetch = qf.select(
            Projections.constructor(
                GroupNotificationQueryModel::class.java,
                notification.id,
                notification.type,
                getGroupInfo(group),
                notification.status,
                getWriter(member),
                notification.content,
                NullExpression<Instant>(Instant::class.java),
                NullExpression<Int>(Int::class.java),
                NullExpression<Int>(Int::class.java),
                notification.createdAt,
                notification.updatedAt,
                NullExpression<Instant>(Instant::class.java)
            ))
            .from(notification)
            .join(group)
                .on(notification.groupId.eq(group.id))
            .join(member)
                .on(notification.memberId.eq(member.memberId)
                    .and(notification.groupId.eq(member.groupId)))
            .where(group.id.eq(groupId)
                .and(lessOrEqualNotificationId(cursorId))
                .and(notification.status.eq(NotificationStatus.PUBLISHED)))
            .limit(pageable.pageSize.toLong())
            .distinct()
            .orderBy(notification.id.desc())
            .fetch()

        return SliceImpl(
            fetch,
            pageable,
            fetch.size == pageable.pageSize
        )
    }

    override fun countTodayGroupNotification(groupId: UUID): Int {
        val notification = QJpaGroupNotification.jpaGroupNotification
        // 쿼리 일시를 기준으로 YYYY-MM-DD 00:00:00 ~ 23:59:59 사이의 데이터를 조회
        val today = Instant.now()

        return qf.select(notification.count())
            .from(notification)
            .where(notification.groupId.eq(groupId)
                .and(notification.createdAt.between(
                    today.truncatedTo(ChronoUnit.DAYS),
                    today.plusSeconds(86399)
                ))
            )
            .fetchOne()
            ?.toInt()
            ?: 0
    }

    override fun findByMemberId(
        memberId: UUID,
        cursorId: UUID?,
        pageable: Pageable
    ): Slice<GroupNotificationQueryModel> {

        // memberId가 속한 그룹들의 알람을 조회한다.
        val notification = QJpaGroupNotification.jpaGroupNotification
        val group = QJpaNotificationGroup.jpaNotificationGroup
        val member =  QJpaGroupMember.jpaGroupMember
        val sender = QJpaGroupMember("sender")

        val fetch = qf.select(
            Projections.constructor(
                GroupNotificationQueryModel::class.java,
                notification.id,
                notification.type,
                getGroupInfo(QJpaNotificationGroup.jpaNotificationGroup),
                notification.status,
                getWriter(sender),
                notification.content,
                NullExpression<Instant>(Instant::class.java),
                NullExpression<Int>(Int::class.java),
                NullExpression<Int>(Int::class.java),
                notification.createdAt,
                notification.updatedAt,
                NullExpression<Instant>(Instant::class.java)
            ))
            .from(member)
            .join(group)
                .on(member.groupId.eq(group.id))
            .join(notification)
                .on(notification.groupId.eq(group.id))
            .join(sender)
                .on(notification.memberId.eq(sender.memberId)
                    .and(notification.groupId.eq(sender.groupId)))
            .where(member.memberId.eq(memberId)
                .and(lessOrEqualNotificationId(cursorId))
                .and(notification.status.eq(NotificationStatus.PUBLISHED)))
            .distinct()
            .orderBy(notification.id.desc())
            .limit(pageable.pageSize.toLong())
            .fetch()

        return SliceImpl(
            fetch,
            pageable,
            fetch.size == pageable.pageSize
        )
    }

    private fun getGroupInfo(
        group: QJpaNotificationGroup
    ): ConstructorExpression<NotificationGroupVo> {
        return Projections.constructor(
            NotificationGroupVo::class.java,
            group.id,
            group.name,
            group.thumbnail
        )
    }

    private fun getWriter(
        member: QJpaGroupMember
    ): ConstructorExpression<Writer> {
        return Projections.constructor(
            Writer::class.java,
            member.memberId,
            member.nickname,
            member.profileImageUrl
        )
    }

    private fun lessOrEqualNotificationId(cursor: UUID?): BooleanExpression {
        val notification = QJpaGroupNotification.jpaGroupNotification
        return if (cursor != null) {
            notification.id.loe(cursor)
        } else {
            Expressions.TRUE
        }
    }
}