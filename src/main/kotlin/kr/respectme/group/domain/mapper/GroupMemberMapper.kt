package kr.respectme.group.domain.mapper

import kr.respectme.group.adapter.out.persistence.entity.JpaGroupMember
import kr.respectme.group.domain.GroupMember
import org.springframework.stereotype.Component

@Component
class GroupMemberMapper {

    fun toDomain(member: JpaGroupMember): GroupMember {
        val groupMember = GroupMember(
            id = member.id!!,
            memberId = member.memberId,
            groupId = member.groupId,
            nickname = member.nickname,
            memberRole = member.memberRole,
            createdAt = member.createdAt,
            profileImageUrl = member.profileImageUrl
        )

        return groupMember
    }

    fun toEntity(member: GroupMember): JpaGroupMember {
        return JpaGroupMember(
            id = member.id,
            memberId = member.getMemberId(),
            groupId = member.getGroupId(),
            nickname = member.getNickname(),
            memberRole = member.getMemberRole(),
            profileImageUrl = member.getProfileImageUrl()
        )
    }
}