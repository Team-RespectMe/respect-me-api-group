package kr.respectme.group.adapter.out.persistence.entity.notifications

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.domain.notifications.NotificationType
import java.time.Instant
import java.time.LocalTime
import java.util.UUID
//
//@Entity
//@DiscriminatorValue("4")
//class JpaDayIntervalNotification(
//    id : UUID = UUIDV7Generator.generate(),
//    groupId: UUID = UUIDV7Generator.generate(),
//    memberId: UUID = UUIDV7Generator.generate(),
//    status: NotificationStatus = NotificationStatus.PENDING,
//    content: String = "",
//    lastSentAt: Instant? = null,
//    dayInterval: Int = 0,
//    triggerTime: LocalTime = LocalTime.now()
//) : JpaGroupNotification(
//    id = id,
//    groupId = groupId,
//    memberId = memberId,
//    content = content,
//    type = NotificationType.REPEATED_INTERVAL,
//    status = status,
//    lastSentAt = lastSentAt,
//) {
//    @Column
//    var dayInterval: Int = dayInterval
//    @Column
//    var triggerTime: LocalTime = triggerTime
//}