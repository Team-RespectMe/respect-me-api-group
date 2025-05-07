package kr.respectme.group.port.`in`.interfaces

import kr.respectme.group.port.`in`.interfaces.dto.GroupMemberCommandResponse
import kr.respectme.group.port.`in`.interfaces.dto.GroupMemberCreateRequest
import kr.respectme.group.port.`in`.interfaces.vo.GroupMemberVo
import java.util.UUID

interface GroupMemberCommandPort {

    fun createGroupMember(loginId: UUID, groupId: UUID, request: GroupMemberCreateRequest): GroupMemberVo

//    fun updateGroupMember(loginId: UUID, groupId: UUID, memberId: UUID, request: GroupMemberCreateRequest): GroupMemberVo

    fun deleteGroupMember(loginId: UUID, groupId: UUID, targetMemberId: UUID)
}