package kr.respectme.group.adapter.out.persistence.repository

import kr.respectme.group.adapter.out.persistence.entity.notifications.JpaGroupNotification
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface JpaGroupNotificationRepository: JpaRepository<JpaGroupNotification, UUID> {

    fun findByIdInAndGroupId(id: List<UUID>, groupId: UUID): List<JpaGroupNotification>
}