package kr.respectme.group.adapter.`in`.interfaces.command

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.respectme.common.annotation.ApplicationResponse
import kr.respectme.common.annotation.LoginMember
import kr.respectme.group.application.dto.group.GroupCreateCommand
import kr.respectme.group.application.dto.group.GroupModifyCommand
import kr.respectme.group.application.command.useCase.NotificationGroupCommandUseCase
import kr.respectme.group.port.`in`.interfaces.NotificationGroupCommandPort
import kr.respectme.group.port.`in`.interfaces.dto.*
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/")
@Tag(name = "Group Command API", description = "알림 그룹 조작 API, 그룹의 생성, 수정, 삭제를 담당 합니다.")
@SecurityRequirement(name = "bearer-jwt")
class RestGroupCommandAdapter(
    private val notificationGroupUseCase: NotificationGroupCommandUseCase
): NotificationGroupCommandPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "알림 그룹 생성", description = "알림 그룹 생성")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "알림 그룹 생성 성공")
        ]
    )
    @PostMapping("notification-groups")
    @ApplicationResponse(status = HttpStatus.CREATED, message = "notification group created.")
    override fun createNotificationGroup(
        @LoginMember loginId: UUID,
        @RequestBody @Valid request: GroupCreateRequest
    ): NotificationGroupResponse {
        val command = GroupCreateCommand.of(request)
        return NotificationGroupResponse.valueOf(
            notificationGroupUseCase.createNotificationGroup(loginId, command)
        )
    }

    @Operation(summary = "알림 그룹 수정", description = "알림 그룹 수정")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "알림 그룹 수정 성공")
        ]
    )
    @PatchMapping("/notification-groups/{groupId}")
    @ApplicationResponse(status = HttpStatus.OK, message = "notification group updated.")
    override fun updateNotificationGroup(
        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID,
        @RequestBody @Valid request: GroupModifyRequest
    ): NotificationGroupResponse {
        val command = GroupModifyCommand.of(request)
        return NotificationGroupResponse.valueOf(
            notificationGroupUseCase.updateNotificationGroup(loginId, groupId, command)
        )
    }

    @Operation(summary = "알림 그룹 삭제", description = "알림 그룹 삭제, 그룹 소유자만 사용 가능, 삭제시 모든 알람도 같이 삭제")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "알림 그룹 삭제 성공")
        ]
    )
    @DeleteMapping("notification-groups/{groupId}")
    override fun deleteNotificationGroup(@LoginMember loginId: UUID, @PathVariable groupId: UUID) {
        notificationGroupUseCase.deleteNotificationGroup(loginId, groupId)
    }
}