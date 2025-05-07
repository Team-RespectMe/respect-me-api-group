package kr.respectme.group.port.`in`.interfaces.vo

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

@Schema(description = "작성자 정보")
data class Writer(
    @Schema(description = "작성자 ID")
    val writerId: UUID = UUID.randomUUID(),
    @Schema(description = "작성자 닉네임, 탈퇴한 회원의 경우 '탈퇴한 회원'으로 표기", example = "홍길동")
    val nickname: String = "",
    @Schema(description = "작성자 프로필 이미지 URL, nullable", nullable = true)
    val thumbnail: String? = null,
) {

}