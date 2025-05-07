package kr.respectme.group.application.dto.member

import kr.respectme.group.domain.GroupMember
import kr.respectme.group.domain.GroupMemberRole
import java.time.Instant
import java.util.UUID

class GroupMemberDto(
    val id: UUID,
    val groupId: UUID,
    val memberId: UUID,
    val nickname: String,
    val profileImageUrl: String?,
    val createdAt: Instant,
    val role: GroupMemberRole
) {

    companion object {

        fun valueOf(entity: GroupMember): GroupMemberDto {
            return GroupMemberDto(
                id = entity.id,
                groupId = entity.getGroupId(),
                memberId = entity.getMemberId(),
                nickname = entity.getNickname(),
                profileImageUrl = null,
                role = entity.getMemberRole(),
                createdAt = entity.getCreatedAt()
            )
        }
    }
}