package kr.respectme.group.adapter.out.persistence.entity.notifications

import jakarta.persistence.*
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.group.adapter.out.persistence.entity.BaseEntity
import kr.respectme.group.adapter.out.persistence.entity.converter.NotificationStateConverter
import kr.respectme.group.adapter.out.persistence.entity.converter.NotificationTypeConverter
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.domain.notifications.NotificationType
import java.time.Instant
import java.util.*

/**
 * Group Notification Jpa Entity
 * @property group JpaNotificationGroup
 * @property member JpaGroupMember
 * @property content contents of the notification
 * @property createdAt Notification created time
 * @property type type of notification (IMMEDIATELY, RESERVED)
 * @property status state of notification (CREATED, PUBLISHED, FAIL)
 */
@Entity(name = "group_notification")
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER)
class JpaGroupNotification(
    id: UUID = UUIDV7Generator.generate(),
    memberId: UUID = UUIDV7Generator.generate(),
    groupId: UUID = UUIDV7Generator.generate(),
    content: String = "",
    type: NotificationType = NotificationType.IMMEDIATE,
    status : NotificationStatus = NotificationStatus.PENDING,
    lastSentAt: Instant? = null,
): BaseEntity<Any?>(id) {

    @Column(name = "sender_id")
    var memberId= memberId

    @JoinColumn(name = "group_id")
    var groupId = groupId

    @Column
    var content: String = content
    @Convert(converter = NotificationTypeConverter::class)
    @Column(name = "type", insertable = false, updatable = false)
    var type: NotificationType = type
    @Convert(converter = NotificationStateConverter::class)
    var status: NotificationStatus = status
    @Column
    var lastSentAt : Instant? = lastSentAt

    override fun toString(): String {
        return "entity : " +
                "id: $id, " +
                "memberId: $memberId, " +
                "groupId: $groupId, " +
                "content: $content, " +
                "type: $type, " +
                "status: $status, " +
                "lastSentAt: $lastSentAt"
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun equals(other: Any?): Boolean {
        if(other == null) return false
        if(this === other) return true
        if(javaClass != other.javaClass) return false
        other as JpaGroupNotification
        return id == other.id
    }
}