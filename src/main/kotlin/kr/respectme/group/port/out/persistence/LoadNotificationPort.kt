package kr.respectme.group.port.out.persistence

import kr.respectme.group.adapter.out.persistence.entity.GroupNotificationQueryModel
import kr.respectme.group.domain.notifications.Notification
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import java.util.*

interface LoadNotificationPort {

    fun loadEntityById(id: UUID): Notification?

    fun findNotificationById(notificationId: UUID): GroupNotificationQueryModel?

    fun findNotificationsByGroupId(groupId: UUID, cursorId:UUID?, pageable: Pageable): Slice<GroupNotificationQueryModel>

    fun findByMemberId(memberId: UUID, cursorId: UUID?, pageable: Pageable): Slice<GroupNotificationQueryModel>

    fun countTodayGroupNotification(groupId: UUID): Int
}