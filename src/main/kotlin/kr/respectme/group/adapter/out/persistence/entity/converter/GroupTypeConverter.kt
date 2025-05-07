package kr.respectme.group.adapter.out.persistence.entity.converter

import jakarta.persistence.AttributeConverter
import kr.respectme.group.domain.GroupType

class GroupTypeConverter: AttributeConverter<GroupType, Int> {

    override fun convertToDatabaseColumn(attribute: GroupType?): Int {
        return attribute!!.dbValue
    }

    override fun convertToEntityAttribute(dbData: Int?): GroupType {
        return GroupType.entries.find { it.dbValue == dbData }
            ?: throw IllegalArgumentException("Unknown GroupType: $dbData")
    }
}