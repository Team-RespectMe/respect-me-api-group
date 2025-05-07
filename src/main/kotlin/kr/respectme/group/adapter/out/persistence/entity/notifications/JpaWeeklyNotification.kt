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
//@DiscriminatorValue("3")
//class JpaWeeklyNotification(
//    id: UUID = UUIDV7Generator.generate(),
//    groupId: UUID = UUIDV7Generator.generate(),
//    memberId: UUID = UUIDV7Generator.generate(),
//    content: String = "",
//    status: NotificationStatus = NotificationStatus.PENDING,
//    dayOfWeeks: Int = 0,
//    triggerTime : LocalTime = LocalTime.now()
//): JpaGroupNotification(
//    id = id,
//    groupId = groupId,
//    memberId = memberId,
//    content = content,
//    status = status,
//    type = NotificationType.REPEATED_WEEKLY,
//) {
//
//    @Column
//    var dayOfWeeks: Int = dayOfWeeks
//
//    @Column
//    var triggerTime: LocalTime = triggerTime
//}