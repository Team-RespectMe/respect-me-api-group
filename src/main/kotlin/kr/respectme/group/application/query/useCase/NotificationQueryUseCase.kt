package kr.respectme.group.application.query.useCase

import kr.respectme.group.application.dto.notification.NotificationCountDto
import kr.respectme.group.application.dto.notification.NotificationQueryModelDto
import org.springframework.data.domain.Pageable
import java.util.UUID

/**
 * NotificationQueryUseCase Inteface
 * Define below methods
 * - getNotification - get a notification with detail information
 * - getNotifications - get a list of notifications with summarized content.
 * - getTodayNotificationCount - get a count of notifications that are created today based on UTC.
 */
interface NotificationQueryUseCase {

    /**
     * get a notification with detail information
     * @param notificationId - notification id
     * @return NotificationDetail
     */
    fun retrieveNotification(memberId: UUID, groupId: UUID, notificationId: UUID): NotificationQueryModelDto

    /**
     * get a list of notifications with summarized content.
     * @param groupId - group id
     * @param page - page number
     * @param size - page size
     * @return List<NotificationSummary>
     */
    fun retrieveGroupNotifications(memberId: UUID,
                                   groupId: UUID,
                                   cursorId: UUID?,
                                   size: Int): List<NotificationQueryModelDto>

    /**
     * get a list of unread notifications with summarized content.
     */
    fun retrieveUnreadNotifications(memberId: UUID, cursorId: UUID?, size: Int): List<NotificationQueryModelDto>

    /**
     * get a count of notifications that are created today based on UTC.
     * @param groupId - group id
     * @return Int
     */
    fun retrieveTodayLeftNotificationCount(groupId: UUID): NotificationCountDto
}