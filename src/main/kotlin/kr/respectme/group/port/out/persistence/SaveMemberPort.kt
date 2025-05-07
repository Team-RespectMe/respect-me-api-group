package kr.respectme.group.port.out.persistence

import kr.respectme.group.domain.GroupMember
import java.util.UUID

interface SaveMemberPort {

    fun save(entity: GroupMember): GroupMember

    fun delete(entity: GroupMember): Unit

    fun delete(groupId: UUID, memberId: UUID): Unit
}