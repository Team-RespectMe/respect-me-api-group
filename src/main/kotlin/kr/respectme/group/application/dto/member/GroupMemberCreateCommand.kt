package kr.respectme.group.application.dto.member

import kr.respectme.group.port.`in`.interfaces.dto.GroupMemberCreateRequest

class GroupMemberCreateCommand(
    val nickname: String,
//    val password: String?
) {

    companion object {

        fun of(request: GroupMemberCreateRequest): GroupMemberCreateCommand {
            return GroupMemberCreateCommand(
                nickname = request.nickname,
//                password = request.groupPassword
            )
        }
    }
}