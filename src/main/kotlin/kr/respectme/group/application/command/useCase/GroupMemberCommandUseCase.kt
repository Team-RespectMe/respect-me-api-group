package kr.respectme.group.application.command.useCase

import kr.respectme.group.application.dto.member.GroupMemberCreateCommand
import kr.respectme.group.application.dto.member.GroupMemberDto
import java.util.UUID

interface GroupMemberCommandUseCase {

    fun addMember(loginId: UUID, groupId: UUID, command: GroupMemberCreateCommand): GroupMemberDto

    fun removeMember(loginId: UUID, groupId: UUID, memberIdToRemove: UUID): Unit
}