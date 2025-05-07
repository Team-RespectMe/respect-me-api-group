package kr.respectme.group.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import kr.respectme.group.common.validator.NullOrLength
import kr.respectme.group.domain.GroupType
import org.hibernate.validator.constraints.Length
import java.util.UUID

@Schema(description = "그룹 수정 요청 객체")
data class GroupModifyRequest(
    @Schema(example = "Group Name", description = "그룹 이름, 수정하지 않는다면 null 또는 field를 제외")
    @NullOrLength(max=12, message = "Group name must be null or less than 12 characters.")
    val name: String?,
    @Schema(example = "Group Description", description = "그룹 설명, 수정하지 않는다면 null 또는 field를 제외")
    @NullOrLength(max=500, message = "Group description must be null or less than 500 characters.")
    val description: String?,
    @Schema(example = "UUID", description = "그룹 소유자, 수정하지 않는다면 null 또는 field를 제외")
    @org.hibernate.validator.constraints.UUID(message = "ownerId must be UUID format.")
    val ownerId: UUID?,
    @Schema(example = "PUBLIC", description = "그룹 타입, 수정하지 않는다면 null 또는 field를 제외")
    val type: GroupType?,
    @Schema(example = "password", description = "그룹 비밀번호, 수정하지 않는다면 null 또는 field를 제외")
    @NullOrLength(max=64, message = "Group password must be null or less than 64 characters.")
    val password: String?,
    @Schema(example = "thumbnail", description = "그룹 썸네일, 썸네일 삭제 시 null 그외 수정은 URL 입력")
    @field: NullOrLength(max=255, message = "Group thumbnail must be null or less than 255 characters.")
    val thumbnail: String?
) {

}