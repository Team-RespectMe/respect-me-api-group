package kr.respectme.group.application.query.useCase

import kr.respectme.group.application.dto.member.GroupMemberDto
import java.util.UUID

interface GroupMemberQueryUseCase {

    fun retrieveGroupMembers(loginId: UUID, groupId: UUID, cursor: UUID?, size: Int): List<GroupMemberDto>

    fun retrieveGroupMember(loginId: UUID, groupId: UUID, memberId: UUID): GroupMemberDto
}