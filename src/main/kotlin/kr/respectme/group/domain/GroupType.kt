package kr.respectme.group.domain

enum class GroupType(val value: String, val dbValue: Int) {
    GROUP_PUBLIC("PUBLIC", 1),
    GROUP_PRIVATE("PRIVATE", 2),
}