package kr.respectme.group.adapter.out.persistence.repository

import jakarta.persistence.LockModeType
import kr.respectme.group.adapter.out.persistence.entity.JpaGroupMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
interface JpaGroupMemberRepository: JpaRepository<JpaGroupMember, UUID> {

    fun findByMemberIdInAndGroupId(memberId: List<UUID>, groupId: UUID): List<JpaGroupMember>

    fun findByGroupIdAndMemberId(groupId: UUID, memberId: UUID): JpaGroupMember?

    fun findByGroupId(groupId: UUID): List<JpaGroupMember>

    @Transactional
    @Modifying
    @Query("DELETE FROM notification_group_member WHERE member_id = :memberId", nativeQuery = true)
    fun deleteAllByMemberId(memberId: UUID)

    @Transactional
    @Modifying
    @Query("UPDATE notification_group_member SET is_deleted = true WHERE member_id = :memberId", nativeQuery = true)
    fun softDeleteByMemberId(memberId: UUID)

    fun findAllByMemberId(memberId: UUID): List<JpaGroupMember>

}