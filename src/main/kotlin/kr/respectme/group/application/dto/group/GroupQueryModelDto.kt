package kr.respectme.group.application.dto.group

import kr.respectme.group.adapter.out.persistence.entity.NotificationGroupQueryModel
import kr.respectme.group.domain.GroupMemberRole
import kr.respectme.group.domain.GroupType
import kr.respectme.group.port.`in`.interfaces.vo.GroupMemberVo
import java.time.Instant
import java.util.UUID


data class GroupQueryModelDto(
    val id: UUID = UUID.randomUUID(),
    val name: String = "",
    val type: GroupType = GroupType.GROUP_PUBLIC,
    val description: String = "",
    val ownerInfo: GroupMemberVo = GroupMemberVo(),
    val memberCount: Int = 0,
    val createdAt: Instant = Instant.now(),
    val thumbnail: String? = null,
    val myRole: GroupMemberRole = GroupMemberRole.MEMBER
){

    companion object {

        fun valueOf(model: NotificationGroupQueryModel) = GroupQueryModelDto(
            id = model.id,
            name = model.name,
            type = model.type,
            description = model.description,
            ownerInfo = model.ownerInfo,
            memberCount = model.memberCount,
            createdAt = model.createdAt,
            thumbnail = model.groupThumbnail,
            myRole = model.myRole
        )
    }
}