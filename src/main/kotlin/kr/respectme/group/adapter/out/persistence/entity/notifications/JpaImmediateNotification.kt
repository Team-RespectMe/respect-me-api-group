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
//@DiscriminatorValue("1")
//class JpaImmediateNotification(
//    id: UUID = UUIDV7Generator.generate(),
//    groupId: UUID = UUIDV7Generator.generate(),
//    memberId: UUID = UUIDV7Generator.generate(),
//    status: NotificationStatus = NotificationStatus.PENDING,
//    content: String = "",
//    lastSentAt: Instant? = null
//) : JpaGroupNotification(
//    id = id,
//    groupId = groupId,
//    memberId = memberId,
//    type = NotificationType.IMMEDIATE,
//    status = status,
//    lastSentAt = lastSentAt,
//    content = content,
//) {
//
//    override fun toString(): String {
//        return super.toString()
//    }
//}