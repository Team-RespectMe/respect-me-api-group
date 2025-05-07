package kr.respectme.group.adapter.out.persistence.entity.converter

import jakarta.persistence.AttributeConverter
import kr.respectme.group.domain.notifications.NotificationType

class NotificationTypeConverter: AttributeConverter<NotificationType, Int> {

    override fun convertToDatabaseColumn(attribute: NotificationType?): Int {
        return attribute!!.dbValue
    }

    override fun convertToEntityAttribute(dbData: Int?): NotificationType {
        return NotificationType.entries.find { it.dbValue == dbData }
            ?: throw IllegalArgumentException("Unknown NotificationType: $dbData")
    }
}