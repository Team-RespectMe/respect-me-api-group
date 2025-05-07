package kr.respectme.group.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.respectme.common.advice.hateoas.*
import kr.respectme.group.application.attachment.AttachmentDto
import kr.respectme.group.configs.MsaConfig
import kr.respectme.group.domain.attachment.AttachmentType
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class AttachmentResponseConverter(
    private val msaConfig: MsaConfig
) : AbstractHateoasConverter<AttachmentResponse>() {

    companion object {
        const val FILE_API_ENDPOINT = "api/v1/files/group-shared"
    }

    override fun translate(element: AttachmentResponse) {
        element._links.addAll(listOf(
            getSelf(element),
            getResource(element)
        ))
    }

    private fun getSelf(element: AttachmentResponse) : HateoasLink {
        return HateoasLink(
            rel = "self",
            href = "${msaConfig.getGatewayUrl()}/api/v1/notification-groups/${element.groupId}/notifications/${element.notificationId}/attachments/${element.id}"
        )
    }

    private fun getResource(element: AttachmentResponse) : HateoasLink {
        val endpoint = getEndpoint(element.type)
        return HateoasLink(
            rel = "resource",
            href = "${msaConfig.getGatewayUrl()}/${getEndpoint(element.type)}/${element.resourceId}"
        )
    }

    private fun getEndpoint(type: AttachmentType): String {
        return when(type) {
            AttachmentType.FILE -> FILE_API_ENDPOINT
            else -> throw IllegalArgumentException("Not supported attachment type")
        }
    }
}

@Hateoas(converter = AttachmentResponseConverter::class)
@Schema(
    name = "AttachmentResponse",
    description = "첨부 리소스 응답 객체",
    title = "첨부 리소스 응답 객체",
    example = "{\n" +
            "    \"id\": \"791763ca-5da8-4aca-9c94-aa9d22a5fd10\",\n" +
            "    \"groupId\": \"791763ca-5da8-4aca-9c94-aa9d22a5fd10\",\n" +
            "    \"notificationId\": \"791763ca-5da8-4aca-9c94-aa9d22a5fd10\",\n" +
            "    \"resourceId\": \"791763ca-5da8-4aca-9c94-aa9d22a5fd10\",\n" +
            "    \"type\": \"FILE\",\n" +
            "    \"_links\": [\n" +
            "        {\n" +
            "            \"rel\": \"self\",\n" +
            "            \"href\": \"https://www.noti-me.net/api/v1/notification-groups/791763ca-5da8-4aca-9c94-aa9d22a5fd10/notifications/791763ca-5da8-4aca-9c94-aa9d22a5fd10/attachments/791763ca-5da8-4aca-9c94-aa9d22a5fd10\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"rel\": \"resource\",\n" +
            "            \"href\": \"https://www.noti-me.net/api/v1/files/group-shared/791763ca-5da8-4aca-9c94-aa9d22a5fd10\"\n" +
            "        }\n" +
            "    ]\n" +
            "}"

)
data class AttachmentResponse(
    @Schema(
        description = "첨부 식별자",
        example = "791763ca-5da8-4aca-9c94-aa9d22a5fd10"
    )
    val id: UUID,
    @Schema(
        description = "그룹 식별자",
        example = "791763ca-5da8-4aca-9c94-aa9d22a5fd10"
    )
    val groupId: UUID,
    @Schema(
        description = "알림 식별자",
        example = "791763ca-5da8-4aca-9c94-aa9d22a5fd10"
    )
    val notificationId: UUID,
    @Schema(
        description = "첨부 리소스 식별자",
        example = "791763ca-5da8-4aca-9c94-aa9d22a5fd10"
    )
    val resourceId: UUID,
    @Schema(
        description = "첨부 리소스 타입",
        example = "FILE"
    )
    val type: AttachmentType,
): HateoasResponse() {

    companion object {
        fun of(dto: AttachmentDto): AttachmentResponse {
            return AttachmentResponse(
                id = dto.id,
                type = dto.type,
                groupId = dto.groupId,
                notificationId = dto.notificationId,
                resourceId = dto.resourceId,
            )
        }
    }
}