package kr.respectme.group.domain.mapper

import kr.respectme.group.adapter.out.persistence.entity.notifications.JpaGroupNotification
import kr.respectme.group.domain.notifications.Notification
//import kr.respectme.group.adapter.out.persistence.entity.notifications.JpaImmediateNotification
//import kr.respectme.group.adapter.out.persistence.entity.notifications.JpaScheduledNotification
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Mapper for notification entity
 */
@Component
class NotificationMapper {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun toDomain(jpaNotification: JpaGroupNotification): Notification {
        val notification = Notification(
            id = jpaNotification.identifier,
            groupId = jpaNotification.groupId,
            senderId = jpaNotification.memberId,
            content = jpaNotification.content,
            status = jpaNotification.status,
            createdAt = jpaNotification.createdAt,
            updatedAt = jpaNotification.updatedAt,
            lastSentAt = jpaNotification.lastSentAt,
        )
        return notification
    }

    fun toEntity(notification: Notification): JpaGroupNotification {
        val entity = JpaGroupNotification(
            id = notification.id,
            groupId = notification.getGroupId(),
            memberId = notification.getSenderId(),
            content = notification.getContent(),
            status = notification.getStatus(),
            lastSentAt = notification.getLastSentAt(),
        )

        return entity
    }

//    /**
//     * create new JpaGroupNotification entity
//     * @param notification: The domain notification to map.
//     */
//    private fun createJpaNotification(notification: Notification): JpaGroupNotification {
//        return when (notification) {
//            is ImmediateNotification -> createJpaImmediateNotification(notification)
//            is ScheduledNotification -> createJpaScheduledNotification(notification)
//            else -> throw IllegalArgumentException("Notification type is not valid")
//        }
//    }

//    /**
//     * create new JpaImmediateNotification entity
//     * @param notification: The domain notification to map.
//     * @param jpaGroup: The JPA notification group entity, only use for search group member
//     */
//    private fun createJpaImmediateNotification(notification: ImmediateNotification): JpaImmediateNotification {
//        val entity = JpaImmediateNotification(
//            id = notification.id,
//            groupId = notification.getGroupId(),
//            memberId = notification.getSenderId(),
//            content = notification.getContent(),
//            status = notification.getStatus(),
//            lastSentAt = notification.getLastSentAt()
//        )
//        return entity
//    }

//    /**
//     * create new JpaScheduledNotification entity
//     * @param notification: The domain notification to map.
//     * @param jpaGroup: The JPA notification group entity, only use for search group member
//     */
//    private fun createJpaScheduledNotification(notification: ScheduledNotification): JpaScheduledNotification {
//        return JpaScheduledNotification(
//            id = notification.id,
//            groupId = notification.getGroupId(),
//            memberId = notification.getSenderId(),
//            content = notification.getContent(),
//            status = notification.getStatus(),
//            lastSentAt = notification.getLastSentAt(),
//            scheduledAt = notification.getScheduledAt(),
//        )
//    }

//    /**
//     * map domain notification entity to JPA notification entity
//     * @param notification: The domain notification to map.
//     * @param jpaNotification: The JPA notification to map to.
//     */
//    private fun castToJpaEntity(notification: Notification, jpaNotification: JpaGroupNotification): JpaGroupNotification {
//        val jpaEntity = when(notification) {
//            is ImmediateNotification -> castToJpaImmediateNotification(notification, jpaNotification)
//            is ScheduledNotification -> castToJpaScheduledNotification(notification, jpaNotification)
//            else -> throw IllegalArgumentException("Notification type is not valid")
//        }
//
//        return jpaEntity
//    }

//    /**
//     * map domain ImmediateNotification entity to JPA ImmediateNotification entity
//     * @param notification: The domain ImmediateNotification to map.
//     * @param jpaNotification: The JPA ImmediateNotification to map to.
//     * @return JpaImmediateNotification
//     */
//    private fun castToJpaImmediateNotification(
//        notification: ImmediateNotification,
//        jpaNotification: JpaGroupNotification
//    ): JpaImmediateNotification {
//        return (jpaNotification as JpaImmediateNotification).apply{
//            this.content = notification.getContent()
//            this.lastSentAt = notification.getLastSentAt()
//        }
//    }
//
//    /**
//     * map domain ScheduledNotification entity to JPA ScheduledNotification entity
//     * @param notification: The domain ScheduledNotification to map.
//     * @param jpaNotification: The JPA ScheduledNotification to map to.
//     * @return JpaScheduledNotification
//     */
//    private fun castToJpaScheduledNotification(
//        notification: ScheduledNotification,
//        jpaNotification: JpaGroupNotification
//    ): JpaScheduledNotification {
//        return (jpaNotification as JpaScheduledNotification).apply{
//            this.content = notification.getContent()
//            this.lastSentAt = notification.getLastSentAt()
//            this.scheduledAt = notification.getScheduledAt()
//        }
//    }
//
//    /**
//     * map JPA ImmediateNotification entity to domain ImmediateNotification entity
//     * @param jpaNotification: The JPA ImmediateNotification to map.
//     * @return ImmediateNotification
//     */
//    private fun castToImmediateNotification(jpaNotification: JpaImmediateNotification): ImmediateNotification {
//        return ImmediateNotification(
//            id = jpaNotification.identifier,
//            groupId = jpaNotification.groupId,
//            senderId = jpaNotification.memberId,
//            content = jpaNotification.content,
//            status = jpaNotification.status,
//            createdAt = jpaNotification.createdAt,
//            updatedAt = jpaNotification.updatedAt,
//            lastSentAt = jpaNotification.lastSentAt,
//        )
//    }
//
//    /**
//     * map JPA ScheduledNotification entity to domain ScheduledNotification entity
//     * @param jpaNotification: The JPA ScheduledNotification to map.
//     * @return ScheduledNotification
//     */
//    private fun castToScheduledNotification(jpaNotification: JpaScheduledNotification): ScheduledNotification {
//        return ScheduledNotification(
//            id = jpaNotification.identifier,
//            groupId = jpaNotification.groupId,
//            senderId = jpaNotification.memberId,
//            content = jpaNotification.content,
//            status = jpaNotification.status,
//            createdAt = jpaNotification.createdAt,
//            updatedAt = jpaNotification.updatedAt,
//            lastSentAt = jpaNotification.lastSentAt,
//            scheduledAt = jpaNotification.scheduledAt,
//        )
//    }
//
//    /**
//     * map JPA ImmediateNotification entity to NotificationDto
//     */
//    fun mapToNotificationDto(jpaNotification: JpaImmediateNotification): NotificationDto {
//        return NotificationDto(
//            notificationId = jpaNotification.identifier,
//            groupId = jpaNotification.groupId,
//            content = jpaNotification.content,
//            type = jpaNotification.type,
//            state = jpaNotification.status,
//            scheduledAt = null,
//            dayOfWeeks = null,
//            dayInterval = null,
//            createdAt = jpaNotification.createdAt,
//            updatedAt = jpaNotification.updatedAt,
//            lastSentAt = jpaNotification.lastSentAt,
//        )
//    }

    /**
     * map JPA ScheduledNotification entity to NotificationDto
     */
//    fun mapToNotificationDto(jpaNotification: JpaScheduledNotification): NotificationDto {
//        return NotificationDto(
//            notificationId = jpaNotification.identifier,
//            groupId = jpaNotification.groupId,
//            content = jpaNotification.content,
//            type = jpaNotification.type,
//            state = jpaNotification.status,
//            scheduledAt = jpaNotification.scheduledAt,
//            createdAt = jpaNotification.createdAt,
//            updatedAt = jpaNotification.updatedAt,
//            lastSentAt = jpaNotification.lastSentAt,
//            dayInterval = null,
//            dayOfWeeks = null,
//        )
//    }
}