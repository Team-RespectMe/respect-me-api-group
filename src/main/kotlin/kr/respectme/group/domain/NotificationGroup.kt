package kr.respectme.group.domain

import kr.respectme.common.domain.BaseDomainEntity
import kr.respectme.common.domain.annotations.DomainEntity
import kr.respectme.common.domain.annotations.DomainRelation
import kr.respectme.common.error.BadRequestException
import kr.respectme.common.error.ForbiddenException
import kr.respectme.common.error.NotFoundException
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.group.common.errors.GroupServiceErrorCode
import kr.respectme.group.domain.notifications.*
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import java.util.*
import javax.swing.GroupLayout.Group

/**
 * Notification Group Entity
 * Domain constraints
 * 1. Notification Group has unique id
 * 2. Notification Group has members, if members empty, group should be deleted
 * 3. Notification Group can be removed, but if the member is admin, only owner can remove.
 * 4. Notification Group should have owner. if owner want to leave the group, owner should change the owner to another member.
 * @property id UUID
 * @property members group members
 * @property notifications group notifications
 * @property createdAt created time
 * @property name group name
 * @property description group description
 * @property ownerId group owner id
 * @property password group password (if group is private required)
 */
@DomainEntity
class NotificationGroup(
    id: UUID = UUIDV7Generator.generate(),
    @DomainRelation
    private var owner: GroupMember = GroupMember(
        groupId = id,
        memberId = UUID.randomUUID(),
        memberRole = GroupMemberRole.OWNER
    ),
    private val createdAt: Instant = Instant.now(),
    private var name: String,
    private var description: String,
    private var password: String? = null,
    private var type: GroupType = GroupType.GROUP_PRIVATE,
    private var thumbnail: String? = null
): BaseDomainEntity<UUID>(id) {

    fun changeThumbnail(thumbnail: String?) {
        this.thumbnail = thumbnail
    }

    fun getThumbnail(): String? {
        return thumbnail
    }

    fun getCreatedAt(): Instant {
        return createdAt
    }

    fun getName(): String {
        return name
    }

    fun getDescription(): String {
        return description
    }

    fun getOwnerId(): UUID {
        return owner.getMemberId()
    }

    fun getOwner(): GroupMember {
        return owner
    }

    fun getPassword(): String? {
        return password
    }

    fun getType(): GroupType {
        return type
    }

    fun setOwner(member: GroupMember) {
        if(member.getGroupId() != this.id) {
           throw BadRequestException(GroupServiceErrorCode.GROUP_MEMBER_NOT_MEMBER)
        }

        if(!member.isGroupOwner()) {
            member.changeMemberRole(GroupMemberRole.OWNER)
        }

        this.owner = member
    }

    fun changeGroupPassword(passwordEncoder: PasswordEncoder, password: String) {
        this.password = passwordEncoder.encode(password)
    }


    fun changeGroupType(groupType: GroupType?) {
        groupType?.let {
            this.type = groupType
        }
    }

    fun changeGroupName(name: String?) {
        name?.let {
            this.name = it
        }
    }

    fun changeGroupDescription(description: String?) {
        description?.let {
            this.description = it
        }
    }

    fun isPrivate(): Boolean {
        return type == GroupType.GROUP_PRIVATE
    }

    fun changePassword(passwordEncoder: PasswordEncoder, password: String?) {
        password?.let{
            this.password = passwordEncoder.encode(password)
        }
    }

    override fun toString(): String {
        return "NotificationGroup(id=$id, owner=$owner, createdAt=$createdAt, name='$name', description='$description', password=$password, type=$type)"
    }
}