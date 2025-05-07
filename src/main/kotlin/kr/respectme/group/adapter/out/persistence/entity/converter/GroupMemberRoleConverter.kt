package kr.respectme.group.adapter.out.persistence.entity.converter

import jakarta.persistence.AttributeConverter
import kr.respectme.group.domain.GroupMemberRole

class GroupMemberRoleConverter: AttributeConverter<GroupMemberRole, Int> {

    override fun convertToDatabaseColumn(attribute: GroupMemberRole?): Int {
        return attribute!!.dbValue
    }

    override fun convertToEntityAttribute(dbData: Int?): GroupMemberRole {
        return GroupMemberRole.entries.find { it.dbValue == dbData }
            ?: throw IllegalArgumentException("Unknown GroupMemberRole: $dbData")
    }
}