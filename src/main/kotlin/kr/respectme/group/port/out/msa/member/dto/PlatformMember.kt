package kr.respectme.group.port.out.msa.member.dto

class PlatformMember(
    val id: String,
    val nickname: String,
    val email: String,
    val isBlocked: Boolean
) {
}