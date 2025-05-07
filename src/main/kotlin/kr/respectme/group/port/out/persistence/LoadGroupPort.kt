package kr.respectme.group.port.out.persistence

import kr.respectme.group.domain.GroupMember
import kr.respectme.group.domain.NotificationGroup
import java.util.UUID

/**
 * GroupLoadPort interface.
 * This interface is used to load NotificationGroup domain entity.
 * The purpose of domain entity loaded by this port is to handle command event(Create, Update, Delete)
 * not for query event.
 */
interface LoadGroupPort {

    /**
     * load NotificationGroup domain entity without members detail.
     * each of group member domain entity has only memberId and groupId, other fields are meaningless
     * @param groupId id of group
     * @param memberIds list of member id want to load
     * @param notificationIds list of notification id want to load
     */
    fun loadGroup(groupId: UUID): NotificationGroup?

    /**
     * load Group Members
     * @param groupId id of group
     */

    fun loadGroupMemberIds(groupId: UUID): List<UUID>
}