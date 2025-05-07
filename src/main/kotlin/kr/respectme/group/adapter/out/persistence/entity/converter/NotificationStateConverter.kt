package kr.respectme.group.adapter.out.persistence.entity.converter

import jakarta.persistence.AttributeConverter
import kr.respectme.group.domain.notifications.NotificationStatus

class NotificationStateConverter
: AttributeConverter<NotificationStatus, Int> {

    override fun convertToDatabaseColumn(attribute: NotificationStatus?): Int {
        return attribute!!.dbValue
    }

    override fun convertToEntityAttribute(dbData: Int?): NotificationStatus {
        return NotificationStatus.entries.find { it.dbValue == dbData }
            ?: throw IllegalArgumentException("Unknown NotificationState: $dbData")
    }
}