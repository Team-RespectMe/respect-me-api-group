package kr.respectme.group.adapter.out.persistence.entity

import jakarta.persistence.*
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.group.adapter.out.persistence.entity.converter.GroupMemberRoleConverter
import kr.respectme.group.domain.GroupMemberRole
import java.io.Serializable
import java.util.*

/**
 * JpaGroupMember Entity
 * @field pk Composite primary key of the entity, groupId and memberId required.
 * @field nickname the member's nickname want to use in the group
 * @field memberRole role of the member in the group
 */
@Entity(name = "notification_group_member")
class JpaGroupMember(
    id: UUID = UUIDV7Generator.generate(),
    @Column
    var groupId: UUID = UUID.randomUUID(),
    @Column
    var memberId: UUID = UUID.randomUUID(),
    @Column
    var nickname: String = "",
    @Convert(converter = GroupMemberRoleConverter::class)
    var memberRole: GroupMemberRole = GroupMemberRole.MEMBER,
    @Column
    var profileImageUrl: String? = null,
    @Column
    var isDeleted : Boolean = false
): BaseEntity<UUID>(id) {

}