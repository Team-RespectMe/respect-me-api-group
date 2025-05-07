package kr.respectme.group.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length
import java.util.UUID

@Schema(description = "그룹 멤버 수정 요청 객체")
data class GroupMemberModifyRequest(
    @field: NotBlank(message = "nickname must not be null or empty.")
    @Schema(required = false, example = "nickname", description = "닉네임")
    val nickname: String? = null
){

}