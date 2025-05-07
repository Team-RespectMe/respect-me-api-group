package kr.respectme.group.application.command

import kr.respectme.common.error.ConflictException
import kr.respectme.common.error.ForbiddenException
import kr.respectme.common.error.NotFoundException
import kr.respectme.group.application.command.useCase.GroupMemberCommandUseCase
import kr.respectme.group.application.dto.member.GroupMemberCreateCommand
import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.common.errors.GroupServiceErrorCode
import kr.respectme.group.domain.GroupMember
import kr.respectme.group.domain.GroupMemberRole
import kr.respectme.group.port.out.persistence.LoadMemberPort
import kr.respectme.group.port.out.persistence.SaveMemberPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class GroupMemberCommandService(
    private val loadMemberPort: LoadMemberPort,
    private val saveMemberPort: SaveMemberPort
): GroupMemberCommandUseCase {

    @Transactional
    override fun addMember(loginId: UUID,
                           groupId: UUID,
                           command: GroupMemberCreateCommand
    ): GroupMemberDto {
        val member = loadMemberPort.load(groupId, loginId)

        if(member != null) {
            throw ConflictException(GroupServiceErrorCode.GROUP_MEMBER_ALREADY_EXISTS)
        }

        val newMember = GroupMember(
            groupId = groupId,
            memberId = loginId,
            nickname = command.nickname,
            memberRole = GroupMemberRole.MEMBER
        )

        val savedMember = saveMemberPort.save(newMember)

        return GroupMemberDto.valueOf(savedMember)
    }

    @Transactional
    override fun removeMember(loginId: UUID, groupId: UUID, memberIdToRemove: UUID) {
        val member = loadMemberPort.load(groupId, memberIdToRemove)
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_MEMBER_NOT_FOUND)

        val loginMember = loadMemberPort.load(groupId, loginId)
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_MEMBER_NOT_FOUND)

        if(!checkPermission(loginMember, member)) {
            throw ForbiddenException(GroupServiceErrorCode.GROUP_MEMBER_NOT_ENOUGH_PERMISSION)
        }

        saveMemberPort.delete(member)
    }

    private fun checkPermission(loginMember: GroupMember, targetMember: GroupMember): Boolean {
        if(loginMember.id == targetMember.id) {
            return true
        }

        // 현재 로그인 한 멤버가 타겟 멤버보다 권한이 높아야 한다.
        if(loginMember.getMemberRole() == GroupMemberRole.OWNER) {
            return true
        }

        if(loginMember.getMemberRole() == GroupMemberRole.ADMIN && targetMember.getMemberRole() == GroupMemberRole.MEMBER) {
            return true
        }

        return false
    }
}