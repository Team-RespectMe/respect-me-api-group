package kr.respectme.group.support

import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.group.adapter.out.persistence.entity.JpaNotificationGroup
import kr.respectme.group.domain.GroupType
//import kr.respectme.group.domain.jpa.JpaGroupMemberEntity
import java.util.*

fun createJpaGroup(size: Int): List<JpaNotificationGroup> {
    return List<JpaNotificationGroup>(size) {
        val groupId = UUIDV7Generator.generate()
        val entity = JpaNotificationGroup(
            name = "group-name-${it}",
            description = "group-description-${it}",
            type = GroupType.GROUP_PRIVATE,
            password = null,
            ownerId = UUID.randomUUID(),
            id = groupId
        )
        entity.description = "group-description-${entity.id}"
        entity.name = "group-name-${entity.id}"
        entity.password = null
        entity.type = GroupType.GROUP_PRIVATE
        entity
    }
}
//
//fun createJpaGroupMember(size: Int, group: JpaNotificationGroup): List<JpaGroupMemberEntity> {
//    return List(size) {
//        val entity = JpaGroupMemberEntity(
//            pk = JpaGroupMemberEntity.PK(group.groupId, UUID.randomUUID()),
//            group = group
//        )
//        entity.role = GroupMemberRole.MEMBER
//        entity.nickname = "nickname-${entity.pk.memberId}"
//        entity.createdAt = LocalDateTime.now()
//
//        entity
//    }
//}

//
//fun createJpaGroupWithId(groupId: UUID): JpaGroupEntity {
//    return JpaGroupEntity(
//        id = groupId,
//        name = "group-${groupId.toString()}",
//        description = "description-${groupId.toString()}",
//        members = mutableListOf(),
//    )
//}
//
//fun createGroupMembers(size: Int, groupId: UUID, role: GroupMemberRole): List<JpaGroupMemberEntity> {
//    return List(size){
//        JpaGroupMemberEntity(
//            pk = JpaGroupMemberEntity.PK(groupId, UUID.randomUUID()),
//            nickname = "nickname-$it",
//            group = createJpaGroupWithId(groupId),
//            role = role
//        )
//    }
//}