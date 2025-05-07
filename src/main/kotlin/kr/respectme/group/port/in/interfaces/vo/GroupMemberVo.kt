package kr.respectme.group.port.`in`.interfaces.vo

import io.swagger.v3.oas.annotations.media.Schema
import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.domain.GroupMember
import kr.respectme.group.domain.GroupMemberRole
import java.time.Instant
import java.util.UUID

@Schema(description = "그룹 멤버 정보")
class GroupMemberVo(
    @Schema(description = "멤버 ID")
    val id : UUID = UUID.randomUUID(),
    @Schema(description = "닉네임")
    val nickname: String = "Random User",
    @Schema(description = "그룹 ID")
    val groupId: UUID = UUID.randomUUID(),
    @Schema(description = "프로필 이미지 URL")
    val profileImage: String? = null,
    @Schema(description = "생성 일시")
    val createdAt: Instant = Instant.now(),
    @Schema(description = "역할", example = "MEMBER")
    val role: GroupMemberRole = GroupMemberRole.MEMBER
) {

    companion object {
        fun valueOf(memberDto: GroupMemberDto): GroupMemberVo {
            return GroupMemberVo(
                id = memberDto.memberId,
                groupId = memberDto.groupId,
                nickname = memberDto.nickname,
                createdAt = memberDto.createdAt,
                profileImage = memberDto.profileImageUrl,
                role = memberDto.role
            )
        }

        fun valueOf(member: GroupMember): GroupMemberVo {
            return GroupMemberVo(
                id = member.id,
                groupId = member.getGroupId(),
                nickname = member.getNickname(),
                createdAt = member.getCreatedAt(),
                profileImage = member.getProfileImageUrl(),
                role = member.getMemberRole()
            )
        }
    }
}