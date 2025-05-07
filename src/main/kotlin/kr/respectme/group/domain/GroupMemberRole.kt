package kr.respectme.group.domain

enum class GroupMemberRole(val roleName: String, val dbValue: Int) {

    OWNER("OWNER", 1),
    ADMIN("ADMIN", 2),
    MEMBER("MEMBER", 3),
    GUEST("GUEST", 4);

    companion object {

        fun fromRoleName(roleName: String): GroupMemberRole {
            return values().firstOrNull { it.roleName == roleName }
                ?: throw IllegalArgumentException("Unknown role name: $roleName")
        }
    }
}