package kr.respectme.group.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Entity
import kr.respectme.common.advice.hateoas.*
import kr.respectme.group.configs.MsaConfig
import kr.respectme.group.domain.GroupMember
import kr.respectme.group.domain.GroupMemberRole
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.UUID

@Component
class GroupMemberCommandResponseConverter(private val msaConfig: MsaConfig) : AbstractHateoasConverter<GroupMemberCommandResponse>() {
    override fun translate(element: GroupMemberCommandResponse) {
        element._links.addAll(listOf(
            HateoasLink(
                rel = "self",
                href = "${msaConfig.getGatewayUrl()}/api/v1/notification-groups/${element.id}/members/${element.id}"
            ),
        ))
    }
}

@Schema(description = "그룹 멤버 커맨드 요청 응답 객체",
    example = "{\n" +
            "    \"id\": \"791763ca-5da8-4aca-9c94-aa9d22a5fd10\",\n" +
            "    \"nickname\": \"주인장\",\n" +
            "    \"profileImage\": \"https://cdn.noti-me.net/profile.jpg\",\n" +
            "    \"createdAt\": \"2023-10-01T00:00:00Z\",\n" +
            "    \"role\": \"MEMBER\",\n" +
            "    \"_links\": [\n" +
            "        {\n" +
            "            \"rel\": \"self\",\n" +
            "            \"href\": \"https://www.noti-me.net/api/v1/notification-groups/{groupId}\n" +
            "        }\n" +
            "    ]\n" +
            "}")
@Hateoas(converter = GroupMemberCommandResponseConverter::class)
data class GroupMemberCommandResponse(
    @Schema(description = "그룹 멤버 식별자", example = "791763ca-5da8-4aca-9c94-aa9d22a5fd10")
    val id: UUID,
    @Schema(description = "그룹 멤버 닉네임", example = "주인장")
    val nickname: String,
    @Schema(description = "그룹 멤버 프로필 이미지", example = "https://cdn.noti-me.net/profile.jpg")
    val profileImage: String?,
    @Schema(description = "그룹 멤버 생성일", example = "2023-10-01T00:00:00Z")
    val createdAt: Instant,
    @Schema(description = "그룹 멤버 역할", example = "MEMBER")
    val role: GroupMemberRole
): HateoasResponse() {

    companion object {
        fun valueOf(entity: GroupMember): GroupMemberCommandResponse {
            return GroupMemberCommandResponse(
                id = entity.id,
                nickname = entity.getNickname(),
                profileImage = null,
                createdAt = entity.getCreatedAt(),
                role = entity.getMemberRole()
            )
        }
    }
}