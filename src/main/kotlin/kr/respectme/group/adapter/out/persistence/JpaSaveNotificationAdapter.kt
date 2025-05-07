package kr.respectme.group.adapter.out.persistence

import kr.respectme.group.adapter.out.persistence.repository.JpaGroupNotificationRepository
import kr.respectme.group.domain.mapper.NotificationMapper
import kr.respectme.group.domain.notifications.Notification
import kr.respectme.group.port.out.persistence.SaveNotificationPort
import org.springframework.stereotype.Service
import java.util.*

@Service
class JpaSaveNotificationAdapter(
    private val notificationRepository: JpaGroupNotificationRepository,
    private val notificationMapper: NotificationMapper
): SaveNotificationPort {

    override fun saveNotification(entity: Notification): Notification {
        val jpaEntity = notificationMapper.toEntity(entity)
        val savedEntity = notificationRepository.save(jpaEntity)
        return notificationMapper.toDomain(savedEntity)
    }

    override fun deleteNotification(id: UUID) {
        notificationRepository.deleteById(id)
    }

    override fun deleteNotification(entity: Notification) {
        notificationRepository.delete(notificationMapper.toEntity(entity))
    }
}