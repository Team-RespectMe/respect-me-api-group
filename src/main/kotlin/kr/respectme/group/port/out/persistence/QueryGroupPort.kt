package kr.respectme.group.port.out.persistence

import kr.respectme.group.adapter.out.persistence.entity.GroupNotificationQueryModel
import kr.respectme.group.adapter.out.persistence.entity.NotificationGroupQueryModel
import kr.respectme.group.application.dto.group.NotificationGroupDto
import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.application.dto.notification.NotificationDto
import java.util.UUID

/**
 * Query Notification Group information include members, notifications
 */
interface QueryGroupPort {

    /**
     * Get NotificationGroup by ID
     * @param groupId NotificationGroup ID
     * @return NotificationGroupDto
     */
    fun getGroup(loginId: UUID, groupId: UUID): NotificationGroupQueryModel?

    /**
     * Get list of groups that login user is member of group
     * @param loginId login user ID
     * @return List<NotificationGroupDto>
     */
    fun getMemberGroups(loginId: UUID): List<NotificationGroupQueryModel>

    /**
     * Get all groups that group type is public
     * @param cursor group id for pagination
     * @param size size for pagination
     * @return List<NotificationGroupDto>
     */
    fun getAllGroups(cursor: UUID?, size: Int?): List<NotificationGroupQueryModel>

    /**
     *
     */
    fun getGroupsByNameContainsKeyword(keyword: String, cursor: UUID?, size: Int): List<NotificationGroupQueryModel>

    fun isPrivateGroup(groupId: UUID): Boolean
}