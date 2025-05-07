package kr.respectme.group.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import kr.respectme.group.domain.attachment.AttachmentType
import java.util.UUID

@Schema(description = "첨부 리소스 생성 요청")
data class AttachmentRequest(
    @Schema(description = "첨부 리소스 타입, FILE, CHAT, VOTE etc...", example = "FILE")
    val type: AttachmentType,
    @Schema(description = "첨부 리소스 아이디", example = "791763ca-5da8-4aca-9c94-aa9d22a5fd10")
    val resourceId: UUID,
) {
}