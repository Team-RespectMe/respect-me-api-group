package kr.respectme.group.adapter.out.persistence.repository

import jakarta.persistence.LockModeType
import kr.respectme.group.adapter.out.persistence.entity.JpaNotificationGroup
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface JpaGroupRepository: org.springframework.data.repository.Repository<JpaNotificationGroup, UUID> {

    fun save(group: JpaNotificationGroup): JpaNotificationGroup

    fun findById(id: UUID): JpaNotificationGroup?

    fun findByOwnerId(ownerId: UUID): List<JpaNotificationGroup>

    fun deleteById(id: UUID)

    @Transactional
    @Modifying
    @Query("DELETE FROM notification_group WHERE id = :groupId", nativeQuery = true)
    fun deleteByGroupId(groupId: UUID)

    @Transactional
    @Modifying
    @Query("DELETE FROM notification_group WHERE owner_id = :ownerId", nativeQuery = true)
    fun deleteByOwnerId(ownerId: UUID)

    @Transactional
    @Modifying
    @Query("UPDATE notification_group SET is_deleted = true WHERE owner_id = :ownerId", nativeQuery = true)
    fun softDeleteByOwnerId(ownerId: UUID)
}