package kr.respectme.group.adapter.out.persistence.entity

import kr.respectme.group.domain.GroupMemberRole
import kr.respectme.group.domain.GroupType
import kr.respectme.group.port.`in`.interfaces.vo.GroupMemberVo
import java.time.Instant
import java.util.*

data class NotificationGroupQueryModel(
    val id: UUID = UUID.randomUUID(),
    val type: GroupType = GroupType.GROUP_PUBLIC,
    val name: String = "",
    val description: String = "",
    var ownerInfo: GroupMemberVo = GroupMemberVo(),
    val memberCount: Int = 0,
    val createdAt: Instant = Instant.now(),
    val groupThumbnail: String? = null,
    var myRole: GroupMemberRole = GroupMemberRole.GUEST
) {

}