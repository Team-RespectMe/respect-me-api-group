package kr.respectme.group.adapter.out.persistence.entity.notifications

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.domain.notifications.NotificationType
import java.time.Instant
import java.util.*
//
//@Entity
//@DiscriminatorValue("2")
//class JpaScheduledNotification(
//    id: UUID = UUIDV7Generator.generate(),
//    groupId: UUID = UUIDV7Generator.generate(),
//    memberId: UUID = UUIDV7Generator.generate(),
//    content: String = "",
//    status: NotificationStatus = NotificationStatus.PENDING,
//    lastSentAt: Instant? = null,
//    scheduledAt : Instant = Instant.now(),
//): JpaGroupNotification(
//    id = id,
//    groupId = groupId,
//    memberId = memberId,
//    content = content,
//    type = NotificationType.SCHEDULED,
//    status = status,
//    lastSentAt = lastSentAt
//) {
//
//    var scheduledAt: Instant = scheduledAt
//}