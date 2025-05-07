package kr.respectme.group.domain

import kr.respectme.common.domain.BaseDomainEntity
import kr.respectme.common.domain.annotations.DomainEntity
import kr.respectme.common.utility.UUIDV7Generator
import java.time.Instant
import java.util.*

@DomainEntity
class GroupMember(
    id: UUID = UUIDV7Generator.generate(),
    private val groupId: UUID = UUID.randomUUID(),
    private val memberId: UUID = UUID.randomUUID(),
    private var nickname: String = "",
    private var memberRole: GroupMemberRole = GroupMemberRole.MEMBER,
    private var profileImageUrl : String? = null,
    private val createdAt: Instant = Instant.now(),
    private var updatedAt: Instant? = null,
    private var isDeleted : Boolean = false // 소프트 삭제용 필드
): BaseDomainEntity<UUID>(id) {

    fun getMemberId(): UUID {
        return memberId
    }

    fun getGroupId(): UUID {
        return groupId
    }

    fun getNickname(): String {
        return nickname
    }

    fun getMemberRole(): GroupMemberRole {
        return memberRole
    }

    fun getProfileImageUrl(): String? {
        return profileImageUrl
    }

    fun getCreatedAt(): Instant {
        return createdAt
    }

    fun getUpdatedAt(): Instant? {
        return updatedAt
    }

    fun getIsDeleted(): Boolean {
        return isDeleted
    }

    fun isGroupOwner(): Boolean {
        return memberRole == GroupMemberRole.OWNER
    }

    fun isGroupAdmin(): Boolean {
        return memberRole == GroupMemberRole.ADMIN
    }

    fun isSameMember(memberId: UUID): Boolean {
        return this.memberId == memberId
    }

    fun isGroupMember(): Boolean {
        return memberRole == GroupMemberRole.MEMBER
    }

    fun changeMemberRole(role: GroupMemberRole?) {
        role?.let{
            this.memberRole = role
        }
    }

    fun changeNickname(nickname: String?) {
        nickname?.let{
            this.nickname = nickname
        }
    }

    fun setIsDeleted(isDeleted: Boolean) {
        this.isDeleted = isDeleted
    }

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false
        other as GroupMember
        return this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "GroupMember(id=$id, groupId=$groupId, memberId=$memberId, nickname='$nickname', memberRole=$memberRole, profileImageUrl=$profileImageUrl, createdAt=$createdAt, updatedAt=$updatedAt, isDeleted=$isDeleted)"
    }
}