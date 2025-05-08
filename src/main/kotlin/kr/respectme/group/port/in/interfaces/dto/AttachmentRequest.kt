package kr.respectme.group.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.respectme.group.domain.attachment.AttachmentType
import java.util.UUID

@Schema(description = "첨부 리소스 생성 요청", example = "{\n" +
        "    \"type\": \"FILE\",\n" +
        "    \"resourceId\": \"0b6f3030-ad06-48a1-be8a-b0fd5b74b5e2\"\n" +
        "}")
data class AttachmentRequest(
    @Schema(description = "첨부 리소스 타입", example = "FILE")
    val type: AttachmentType,
    @Schema(description = "첨부 리소스 아이디", example = "0b6f3030-ad06-48a1-be8a-b0fd5b74b5e2")
    val resourceId: UUID,
) {
}