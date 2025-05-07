package kr.respectme.group.domain.attachment

enum class AttachmentType(val value: Int) {
    FILE(1),
    EXTERNAL_URL(2),
    VOTE(3),
    CHAT(4)
}