package kr.respectme.group.port.out.persistence

import kr.respectme.group.domain.GroupMember
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import java.util.*

interface LoadMemberPort  {

    fun load(groupId: UUID, memberId: UUID): GroupMember?

    fun findMemberIdsByGroupId(groupId: UUID): List<UUID>

    fun findByGroupIdAndMemberId(groupId: UUID, memberId: UUID): GroupMember?

    fun findByMemberId(memberId: UUID): List<GroupMember>

    fun findAllByGroupId(groupId: UUID, cursor: UUID?, pageable: Pageable): Slice<GroupMember>
}