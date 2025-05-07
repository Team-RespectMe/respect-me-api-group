package kr.respectme.group.domain.notifications

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import kr.respectme.common.domain.BaseDomainEntity
import kr.respectme.common.domain.annotations.DomainEntity
import kr.respectme.common.domain.annotations.DomainRelation
import kr.respectme.common.error.BadRequestException
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.group.application.attachment.LinkAttachmentCommand
import kr.respectme.group.common.errors.GroupServiceErrorCode
import kr.respectme.group.domain.attachment.Attachment
import kr.respectme.group.domain.notifications.NotificationType.*
import kr.respectme.group.port.out.persistence.LoadAttachmentPort
import kr.respectme.group.port.out.persistence.SaveAttachmentPort
import org.springframework.security.config.BeanIds
import java.time.Instant
import java.util.*

@DomainEntity
class Notification(
    id: UUID = UUIDV7Generator.generate(),
    private val groupId: UUID = UUID.randomUUID(),
    private val senderId: UUID = UUID.randomUUID(),
    private var content: String = "",
    private var status : NotificationStatus = NotificationStatus.PENDING,
    private var type: NotificationType = IMMEDIATE,
    private val createdAt: Instant = Instant.now(),
    private var updatedAt: Instant? = null,
    private var lastSentAt: Instant? = null,
): BaseDomainEntity<UUID>(id) {


    fun getGroupId(): UUID {
        return groupId
    }

    fun getSenderId(): UUID {
        return senderId
    }

    fun getContent(): String {
        return content
    }

    fun getStatus(): NotificationStatus {
        return status
    }

    fun getType(): NotificationType {
        return type
    }

    fun getCreatedAt(): Instant {
        return createdAt
    }

    fun getUpdatedAt(): Instant? {
        return updatedAt
    }

    fun getLastSentAt(): Instant? {
        return lastSentAt
    }

    fun updateContent(content: String?) {
//        if(status == NotificationStatus.PUBLISHED) {
//            throw BadRequestException(GroupServiceErrorCode.GROUP_NOTIFICATION_CANNOT_UPDATE_CONTENTS)
//        }

        if(content == null) return
        this.content = content
        this.updatedAt = Instant.now()
    }

    fun updateStatus(status: NotificationStatus?) {
        status?.takeIf { it != this.status } ?: return
        this.status = status
        if(status == NotificationStatus.PUBLISHED) {
            this.lastSentAt = Instant.now()
        }
        updateTime()
//        updated()
    }

    fun switchType(type: NotificationType) {
        this.type = type
        updateTime()
//        updated()
    }

    protected fun updateTime() {
        this.updatedAt = Instant.now()
    }

    fun validate() {
        validateContent()
        validateType()
    }

    private fun validateContent() {
        if (content.isBlank()) {
            throw IllegalArgumentException("Notification content should not be empty")
        }
    }

    private fun validateType() {
        if(this.getType() != NotificationType.IMMEDIATE) {
            throw IllegalArgumentException("Notification type is not immediate")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Notification) return false

        if (groupId != other.groupId) return false
        if (senderId != other.senderId) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }

    override fun toString(): String {
        return "Notification(id=$id, groupId=$groupId, senderId=$senderId, content='$content', status=$status, type=$type, createdAt=$createdAt, updatedAt=$updatedAt, lastSentAt=$lastSentAt)"
    }
}