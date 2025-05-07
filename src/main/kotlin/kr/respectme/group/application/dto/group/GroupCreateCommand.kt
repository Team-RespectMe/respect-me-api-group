package kr.respectme.group.application.dto.group

import kr.respectme.group.domain.GroupType
import kr.respectme.group.port.`in`.interfaces.dto.GroupCreateRequest

data class GroupCreateCommand(
    val groupType: GroupType,
    val groupName: String,
    val groupDescription: String,
    val groupOwnerNickname: String,
    val groupPassword: String?=null,
    val groupImageUrl: String?=null
) {

    companion object {

        fun of(request: GroupCreateRequest): GroupCreateCommand {
            return GroupCreateCommand(
                groupType = request.type!!,
                groupName = request.name,
                groupDescription = request.description,
                groupOwnerNickname = request.ownerNickname,
                groupImageUrl = request.thumbnail,
            )
        }
    }
}