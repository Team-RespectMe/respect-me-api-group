package kr.respectme.group.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.respectme.common.advice.hateoas.*
import kr.respectme.group.application.dto.notification.NotificationQueryModelDto
import kr.respectme.group.configs.MsaConfig
import kr.respectme.group.domain.notifications.DayOfWeek
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.domain.notifications.NotificationType
import kr.respectme.group.port.`in`.interfaces.vo.NotificationGroupVo
import kr.respectme.group.port.`in`.interfaces.vo.Writer
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
class GroupNotificationQueryResponseConverter(
    private val msaConfig: MsaConfig
) : AbstractHateoasConverter<GroupNotificationQueryResponse>() {

    override fun translate(element: GroupNotificationQueryResponse) {
        element._links.addAll(listOf(
            getSelf(element),
            getGroup(element),
            getAttachments(element)
        ))
    }

    private fun getSelf(element: GroupNotificationQueryResponse): HateoasLink {
        return HateoasLink(
            rel = "self",
            href = "${msaConfig.getGatewayUrl()}/api/v1/notification-groups/${element.groupInfo.id}/notifications/${element.notificationId}"
        )
    }

    private fun getGroup(element: GroupNotificationQueryResponse): HateoasLink {
        return HateoasLink(
            rel = "group",
            href = "${msaConfig.getGatewayUrl()}/api/v1/notification-groups/${element.groupInfo.id}"
        )
    }

    private fun getAttachments(element: GroupNotificationQueryResponse): HateoasLink {
        return HateoasLink(
            rel = "attachments",
            href = "${msaConfig.getGatewayUrl()}/api/v1/notification-groups/${element.groupInfo.id}/notifications/${element.notificationId}/attachments"
        )
    }
}

@Hateoas(converter = GroupNotificationQueryResponseConverter::class)
@Schema(description = "그룹 Notification 정보",
    example = "{\n" +
            "    \"notificationId\": \"791763ca-5da8-4aca-9c94-aa9d22a5fd10\",\n" +
            "    \"groupInfo\": {\n" +
            "        \"id\": \"791763ca-5da8-4aca-9c94-aa9d22a5fd10\",\n" +
            "        \"name\": \"그룹 이름\"\n" +
            "    },\n" +
            "    \"writer\": {\n" +
            "        \"id\": \"791763ca-5da8-4aca-9c94-aa9d22a5fd10\",\n" +
            "        \"nickname\": \"주인장\"\n" +
            "    },\n" +
            "    \"content\": \"Notification Content\",\n" +
            "    \"type\": \"IMMEDIATE\",\n" +
            "    \"status\": \"PENDING\",\n" +
            "    \"createdAt\": \"2023-10-01T00:00:00Z\",\n" +
            "    \"updatedAt\": null,\n" +
            "    \"scheduledAt\": null,\n" +
            "    \"dayOfWeeks\": [],\n" +
            "    \"dayInterval\": null,\n" +
            "    \"lastSentAt\": null,\n" +
            "    \"attachments\": [],\n" +
            "    \"_links\": [\n" +
            "        {\n" +
            "            \"rel\": \"self\",\n" +
            "            \"href\": \"https://www.noti-me.net/api/v1/notification-groups/791763ca-5da8-4aca-9c94-aa9d22a5fd10/notifications/791763ca-5da8-4aca-9c94-aa9d22a5fd10\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"rel\": \"group\",\n" +
            "            \"href\": \"https://www.noti-me.net/api/v1/notification-groups/791763ca-5da8-4aca-9c94-aa9d22a5fd10\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"rel\": \"attachments\",\n" +
            "            \"href\": \"https://www.noti-me.net/api/v1/notification-groups/791763ca-5da8-4aca-9c94-aa9d22a5fd10/notifications/791763ca-5da8-4aca-9c94-aa9d22a5fd10/attachments\"\n" +
            "        }\n" +
            "    ]\n" +
        "}"
)
data class GroupNotificationQueryResponse(
    @Schema(description = "Notification ID")
    val notificationId: UUID,
    @Schema(description = "그룹 ID")
    val groupInfo: NotificationGroupVo = NotificationGroupVo(),
    @Schema(description = "작성자 정보")
    val writer: Writer = Writer(),
    @Schema(description = "Notification 내용")
    val content: String = "",
    @Schema(description = "Notification 타입", examples = ["IMMEDIATE", "SCHEDULED", "REPEATED_WEEKLY", "REPEATED_INTERVAL"])
    val type: NotificationType,
    @Schema(description = "Notification 상태", example = "PENDING")
    val status: NotificationStatus,
    @Schema(description = "Notification 생성 일시")
    val createdAt: Instant,
    @Schema(description = "Notification 수정 일시")
    val updatedAt: Instant?,
    @Schema(description = "Notification 예약 일시(TYPE = SCHEDULED)의 경우")
    val scheduledAt: Instant? = null,
    @Schema(description = "Notification 요일, 매주 반복일 경우(TYPE = REPEATED_WEEKLY)")
    val dayOfWeeks: List<DayOfWeek> = emptyList(),
    @Schema(description = "Notification 간격, 매 n일 반복일 경우(TYPE = REPEATED_INTERVAL)")
    val dayInterval: Int? = null,
    @Schema(description = "Notification 마지막 전송 일시")
    val lastSentAt: Instant? = null,
    @Schema
    val attachments: MutableList<AttachmentResponse> = mutableListOf()
) : HateoasResponse() {


    companion object {

        fun valueOf(notification: NotificationQueryModelDto): GroupNotificationQueryResponse {
            return GroupNotificationQueryResponse(
                notificationId = notification.id,
                groupInfo = notification.groupInfo,
                writer = notification.writer,
                content = notification.content,
                type = notification.type,
                status = notification.status,
                createdAt = notification.createdAt,
                updatedAt = notification.updatedAt,
                scheduledAt = notification.scheduledAt,
                dayOfWeeks = notification.dayOfWeeks,
                dayInterval = notification.dayInterval,
                lastSentAt = notification.lastSentAt
            )
        }
    }
}