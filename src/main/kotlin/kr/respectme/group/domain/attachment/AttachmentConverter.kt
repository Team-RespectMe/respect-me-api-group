package kr.respectme.group.domain.attachment

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class AttachmentConverter : AttributeConverter<AttachmentType, Int> {

    override fun convertToDatabaseColumn(attribute: AttachmentType?): Int? {
        return attribute?.value
    }

    override fun convertToEntityAttribute(dbData: Int?): AttachmentType? {
        return dbData.let {
            AttachmentType.entries.firstOrNull { type -> type.value == it }
        }
    }
}