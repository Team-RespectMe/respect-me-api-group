package kr.respectme.group.application.query.useCase

import kr.respectme.group.application.dto.group.GroupQueryModelDto
import kr.respectme.group.application.dto.group.GroupSearchParams
import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.application.dto.notification.NotificationQueryModelDto
import java.util.UUID

/***
 * NotificationGroup UseCase for only query
 */
interface NotificationGroupQueryUseCase {

//    /**
//     * retrieve group member
//     * @param loginId login user id
//     * @param groupId group id
//     * @param memberId target member id want to retrieve info
//     * @return GroupMemberDto
//     */
//    fun retrieveGroupMember(loginId: UUID, groupId: UUID, memberId: UUID): GroupMemberDto
//
//    /**
//     * retrieve group members
//     * if group doesn't contain login user, throw ForbiddenException
//     * @param loginId login user id
//     * @param groupId group id
//     * @return List<GroupMemberDto>
//     */
//    fun retrieveGroupMembers(loginId: UUID, groupId: UUID): List<GroupMemberDto>

//    /**
//     * retrieve group notifications
//     * if group doesn't contain login user, throw ForbiddenException
//     * @param loginId login user id
//     * @param groupId group id
//     * @param cursor cursor for pagination
//     * @param size size for pagination
//     * @return List<NotificationDto>
//     */
//    fun retrieveGroupNotifications(loginId: UUID, groupId: UUID, cursor: UUID?, size: Int): List<NotificationQueryModelDto>

    /**
     * retrieve group by given group id
     * if group is private, check login user is member of group
     * if not, throw ForbiddenException
     * @param loginId login user id
     * @param groupId group id
     * @return NotificationGroupDto
     */
    fun retrieveGroup(loginId: UUID, groupId: UUID): GroupQueryModelDto

    /**
     * retrieve groups that login user is member of group
     * @param loginId login user id
     * @return List<NotificationGroupDto>
     */
    fun retrieveMemberGroups(loginId: UUID): List<GroupQueryModelDto>

    /**
     * retrieve all groups that group type is public.
     * @param searchParams search parameters
     * @param cursor cursor for pagination
     * @param size size for pagination
     * @return List<NotificationGroupDto>
     */
    fun retrieveGroupsBySearchParam(searchParams: GroupSearchParams,
                                    cursor: UUID?,
                                    size: Int?): List<GroupQueryModelDto>

//    /**
//     * 특정 멤버가 속한 그룹의 모든 알림을 조회한다.
//     * @param loginId 로그인한 사용자의 ID
//     * @param cursor 페이징을 위한 커서
//     * @param size 페이징을 위한 사이즈
//     * @return List<NotificationDto>
//     */
//    fun retrieveMemberNotifications(loginId: UUID, cursor: UUID?, size: Int): List<NotificationQueryModelDto>

//    /**
//     * 특정 알림을 상세 조회한다.
//     * @param loginId 로그인한 사용자의 ID
//     * @param groupId 그룹 ID
//     * @param notificationId 알림 ID
//     * @return NotificationDto
//     */
//    fun retrieveNotification(loginId: UUID, groupId: UUID, notificationId: UUID): NotificationQueryModelDto
}