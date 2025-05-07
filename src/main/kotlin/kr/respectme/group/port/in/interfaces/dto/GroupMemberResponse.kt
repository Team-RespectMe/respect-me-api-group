package kr.respectme.group.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.respectme.common.advice.hateoas.AbstractHateoasConverter
import kr.respectme.common.advice.hateoas.Hateoas
import kr.respectme.common.advice.hateoas.HateoasLink
import kr.respectme.common.advice.hateoas.HateoasResponse
import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.configs.MsaConfig
import kr.respectme.group.domain.GroupMemberRole
import org.springframework.stereotype.Component
import java.util.*

@Component
class GroupMemberResponseConverter(private val msaConfig: MsaConfig) : AbstractHateoasConverter<GroupMemberResponse>() {
    override fun translate(element: GroupMemberResponse) {
        element._links.addAll(listOf(
            HateoasLink(
                rel = "self",
                href = "${msaConfig.getGatewayUrl()}/api/v1/notification-groups/${element.groupId}/members/${element.memberId}"
            )
        ))
    }
}

@Hateoas(converter = GroupMemberResponseConverter::class)
@Schema(description = "그룹 멤버 응답 객체",
    example = "{\n" +
            "    \"groupId\": \"791763ca-5da8-4aca-9c94-aa9d22a5fd10\",\n" +
            "    \"memberId\": \"791763ca-5da8-4aca-9c94-aa9d22a5fd10\",\n" +
            "    \"nickname\": \"주인장\",\n" +
            "    \"role\": \"MEMBER\",\n" +
            "    \"_links\": [\n" +
            "        {\n" +
            "            \"rel\": \"self\",\n" +
            "            \"href\": \"https://www.noti-me.net/api/v1/notification-groups/{groupId}/members/{memberId}\"\n" +
            "        }\n" +
            "    ]\n" +
            "}")
data class GroupMemberResponse(
    @Schema(description = "그룹 ID")
    val groupId: UUID,
    @Schema(description = "멤버 ID")
    val memberId: UUID,
    @Schema(description = "닉네임")
    val nickname: String,
    @Schema(description = "역할", example = "MEMBER")
    val role: GroupMemberRole
): HateoasResponse() {

    companion object {
        fun of(dto: GroupMemberDto): GroupMemberResponse {
            return GroupMemberResponse(
                groupId = dto.groupId,
                memberId = dto.memberId,
                nickname = dto.nickname,
                role = dto.role
            )
        }
    }
}