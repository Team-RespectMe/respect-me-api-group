package kr.respectme.group.adapter.`in`.interfaces.command

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import jakarta.validation.Valid
import kr.respectme.common.annotation.ApplicationResponse
import kr.respectme.common.annotation.LoginMember
import kr.respectme.group.application.command.useCase.GroupMemberCommandUseCase
import kr.respectme.group.application.dto.member.GroupMemberCreateCommand
import kr.respectme.group.port.`in`.interfaces.GroupMemberCommandPort
import kr.respectme.group.port.`in`.interfaces.dto.GroupMemberCreateRequest
import kr.respectme.group.port.`in`.interfaces.vo.GroupMemberVo
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/")
@Tags(Tag(name = "Group Member Command API", description = "그룹 멤버 Command API, 그룹 멤버의 생성, 수정, 삭제를 담당 합니다."))
class RestGroupMemberCommandAdapter(
    private val memberCommandUseCase: GroupMemberCommandUseCase
): GroupMemberCommandPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "그룹 멤버 추가", description = "그룹 멤버 추가")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "그룹 멤버 추가 성공")
        ]
    )
    @PostMapping("notification-groups/{groupId}/members")
    @ApplicationResponse(status = HttpStatus.CREATED, message = "group member added.")
    override fun createGroupMember (
        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID,
        @RequestBody @Valid request: GroupMemberCreateRequest
    ): GroupMemberVo {
        val command = GroupMemberCreateCommand.of(request)
        return GroupMemberVo.valueOf(
            memberCommandUseCase.addMember(loginId, groupId, command)
        )
    }

    @Operation(summary = "그룹 멤버 삭제", description = "그룹 멤버 삭제")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "그룹 멤버 삭제 성공")
        ]
    )
    @DeleteMapping("notification-groups/{groupId}/members/{targetMemberId}")
    @ApplicationResponse(status = HttpStatus.NO_CONTENT, message = "group member removed.")
    override fun deleteGroupMember(
        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID,
        @PathVariable targetMemberId: UUID) {
        return memberCommandUseCase.removeMember(loginId, groupId, targetMemberId)
    }
}