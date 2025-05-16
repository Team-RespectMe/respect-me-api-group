package kr.respectme.group.adapter.`in`.interfaces.command

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.respectme.common.annotation.ApplicationResponse
import kr.respectme.common.annotation.LoginMember
import kr.respectme.group.application.attachment.AttachmentHandler
import kr.respectme.group.application.attachment.LinkAttachmentCommand
import kr.respectme.group.application.attachment.LinkAttachmentManager
import kr.respectme.group.application.command.useCase.NotificationCommandUseCase
import kr.respectme.group.application.dto.notification.NotificationCreateCommand
import kr.respectme.group.application.dto.notification.NotificationModifyCommand
import kr.respectme.group.port.`in`.interfaces.NotificationCommandPort
import kr.respectme.group.port.`in`.interfaces.dto.NotificationCommandResponse
import kr.respectme.group.port.`in`.interfaces.dto.NotificationCreateRequest
import kr.respectme.group.port.`in`.interfaces.dto.NotificationModifyRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/")
@Tag(name = "Notification Command API", description = "그룹 알림 조작용 API, 생성, 수정, 삭제를 담당 합니다.")
class RestNotificationCommandAdapter(
    private val notificationCommandUseCase: NotificationCommandUseCase,
    private val attachmentManager: LinkAttachmentManager,
): NotificationCommandPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "알림 생성", description = "알림 생성")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "알림 생성 성공")
        ]
    )
    @PostMapping("notification-groups/{groupId}/notifications")
    @ApplicationResponse(status = HttpStatus.CREATED, message = "notification created.")
    override fun createNotification(
        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID,
        @RequestBody @Valid request: NotificationCreateRequest
    ): NotificationCommandResponse {
        val command = NotificationCreateCommand.valueOf(groupId, loginId, request)
        val notification = NotificationCommandResponse.valueOf(
            notificationCommandUseCase.createNotification(loginId, groupId, command)
        )
//        request.attachments.forEach { attachmentRequest ->
//            attachmentManager.link(loginId, LnkAttachmentCommand.of(groupId, notification.notificationId, attachmentRequest))
//        }
        attachmentManager.link(loginId, groupId, notification.notificationId,
            request.attachments.map { attachmentRequest ->
                LinkAttachmentCommand.of(groupId=groupId,
                    notificationId = notification.notificationId,
                    request = attachmentRequest
                )
            }.toList()
        )

        return notification
    }

    @Operation(summary = "알림의 본문을 수정한다.", description = "알림의 본문을 수정한다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "알림 본문 수정 성공"),
    ])
    @PatchMapping("notification-groups/{groupId}/notifications/{notificationId}/content")
    @ApplicationResponse(status = HttpStatus.OK, message = "notification content updated.")
    override fun updateNotificationContent(
        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID,
        @PathVariable notificationId: UUID,
        @RequestBody @Valid request: NotificationModifyRequest
    ): NotificationCommandResponse {
        val command = NotificationModifyCommand.valueOf(request)
        return NotificationCommandResponse.valueOf(
            notificationCommandUseCase.updateNotification(loginId, groupId, notificationId, command)
        )
    }

    @Operation(summary = "알림의 본문과 첨부 정보를 함께 수정", description = "알림의 본문과 첨부 정보를 함께 수정합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "알림 본문 및 첨부 정보 수정 성공"),
    ])
    @PatchMapping("notification-groups/{groupId}/notifications/{notificationId}")
    @ApplicationResponse(status = HttpStatus.OK, message = "notification content and attachments updated.")
    override fun updateNotification(
        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID,
        @PathVariable notificationId: UUID,
        @RequestBody @Valid request: NotificationModifyRequest
    ): NotificationCommandResponse {
        val command = NotificationModifyCommand.valueOf(request)
        val notification = NotificationCommandResponse.valueOf(
            notificationCommandUseCase.updateNotification(loginId, groupId, notificationId, command)
        )

        val attachments = if (request.attachments.isEmpty()) {
            emptyList()
        } else {
            request.attachments.map { attachmentRequest ->
                LinkAttachmentCommand.of(groupId=groupId,
                    notificationId = notification.notificationId,
                    request = attachmentRequest
                )
            }.toList()
        }

        attachmentManager.link(loginId, groupId, notificationId, attachments)

        return notification
    }

    @Operation(summary = "알림 단건 삭제", description = "지정된 그룹의 알림 하나를 삭제합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "삭제 성공"),
    ])
    @DeleteMapping("notification-groups/{groupId}/notifications/{notificationId}")
    override fun deleteNotification(
        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID,
        @PathVariable notificationId: UUID) {
        notificationCommandUseCase.deleteNotification(loginId, groupId, notificationId)
    }
}