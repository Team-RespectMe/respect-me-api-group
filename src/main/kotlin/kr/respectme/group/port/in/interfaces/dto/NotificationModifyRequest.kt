package kr.respectme.group.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank


@Schema(description = "알림 생성 요청 객체")
data class NotificationModifyRequest(
    @Schema(required = true, example = "Notification Content", description = "알람 본문")
    @field: NotBlank(message = "Notification content must not be null or empty.")
    val content: String,
    @Schema(required = true,
        description = "첨부 리소스 정보, 수정 내역이 없더라도 반드시 기존 필드를 포함시켜야 합니다. 그렇지 않으면 모든 첨부 점보가 제거됩니다.",)
//        example = "[{\n"+
//                "    \"type\": \"FILE\",\n" +
//                "    \"resourceId\": \"첨부 파일 URL\"\n" +
//                "}]")
    val attachments: List<AttachmentRequest> = emptyList()
) {
}