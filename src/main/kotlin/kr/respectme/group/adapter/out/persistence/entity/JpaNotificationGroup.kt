package kr.respectme.group.adapter.out.persistence.entity

import jakarta.persistence.*
import kr.respectme.group.adapter.out.persistence.entity.converter.GroupTypeConverter
import kr.respectme.group.domain.GroupType
import java.util.*

/**
 * Group Jpa Entity
 * @property name name of group
 * @property description group description
 * @property ownerId UUID
 * @property password password to join this group, nullable
 * @property members MutableSet<JpaGroupMember>
 *     @see JpaGroupMember
 * @property notifications MutableSet<JpaGroupNotification>
 *     @see JpaGroupNotification
 * @property type type of group
 *     @see GroupType
 */
@Entity(name = "notification_group")
class JpaNotificationGroup(
    id: UUID?= null,
    @Column(length=512)
    var description: String = "",
    @Column(length=12)
    var name: String = "",
    @Column(length=255)
    var password: String? = null,
    @Convert(converter = GroupTypeConverter::class)
    var type: GroupType = GroupType.GROUP_PRIVATE,
    @Column
    var ownerId: UUID = UUID.randomUUID(),
    @Column
    var isDeleted: Boolean = false,
    @Column(nullable = true)
    var thumbnail: String? = null
): BaseEntity<Any?>(id) {

}
