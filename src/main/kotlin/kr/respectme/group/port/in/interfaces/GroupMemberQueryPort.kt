package kr.respectme.group.port.`in`.interfaces

import kr.respectme.group.port.`in`.interfaces.vo.GroupMemberVo
import java.util.UUID


interface GroupMemberQueryPort {

    fun getGroupMembers(loginId: UUID, groupId: UUID, cursor: UUID?, size: Int?): List<GroupMemberVo>

    fun getGroupMember(loginId: UUID, groupId: UUID, memberId: UUID): GroupMemberVo
}