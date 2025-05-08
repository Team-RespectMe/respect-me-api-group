package kr.respectme.group.adapter.`in`.interfaces

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import kr.respectme.common.annotation.ApplicationResponse
import kr.respectme.common.annotation.CursorPagination
import kr.respectme.common.annotation.LoginMember
import kr.respectme.group.application.attachment.LinkAttachmentCommand
import kr.respectme.group.application.attachment.LinkAttachmentManager
import kr.respectme.group.port.`in`.interfaces.AttachmentPort
import kr.respectme.group.port.`in`.interfaces.dto.AttachmentRequest
import kr.respectme.group.port.`in`.interfaces.dto.AttachmentResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@Tag(name = "Attachment API", description = "알림 첨부 리소스 관리 API")
@RequestMapping("/api/v1/")
class HttpAttachmentAdapter(
    private val attachmentManager: LinkAttachmentManager
) : AttachmentPort {

    @Operation(summary = "알림에 첨부된 모든 리소스를 조회합니다.", description = "알림에 첨부된 모든 리소스를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "첨부 리소스 조회 성공")
    @GetMapping("notification-groups/{groupId}/notifications/{notificationId}/attachments")
    @CursorPagination
    @ApplicationResponse(
        status = HttpStatus.OK,
        message = "첨부 리소스 조회 성공"
    )
    override fun getAttachments(@LoginMember loginId: UUID,
                                @PathVariable groupId: UUID,
                                @PathVariable notificationId: UUID): List<AttachmentResponse> {
        return attachmentManager.getAttachments(loginId, groupId, notificationId)
            .map { AttachmentResponse.of(it) }
    }

    @PostMapping("notification-groups/{groupId}/notifications/{notificationId}/attachments")
    @ApiResponse(responseCode = "201", description = "첨부 리소스 링크 성공")
    @Operation(summary = "첨부 리소스 링크", description = "알림에 리소스를 첨부한다.")
    @ApplicationResponse(
        status = HttpStatus.CREATED,
        message = "첨부 리소스 링크 성공"
    )
    override fun linkAttachment(@LoginMember loginId: UUID,
                                @PathVariable groupId: UUID,
                                @PathVariable notificationId: UUID,
                                @RequestBody request: AttachmentRequest
    ): AttachmentResponse {
        val result = attachmentManager.link(loginId, listOf(LinkAttachmentCommand(
            notificationId = notificationId,
            groupId = groupId,
            type = request.type,
            resourceId = request.resourceId
        )))

        return AttachmentResponse.of(result[0])
    }

    @DeleteMapping("notification-groups/{groupId}/notifications/{notificationId}/attachments/{attachmentId}")
    @ApiResponse(responseCode = "204", description = "첨부 리소스 링크 해제 성공")
    @Operation(summary = "첨부 리소스 링크", description = "알림에 첨부된 리소스를 해제한다.")
    @ApplicationResponse(
        status = HttpStatus.NO_CONTENT,
        message = "첨부 리소스 링크 해제 성공"
    )
    override fun unlinkAttachment(@LoginMember loginId: UUID,
                                  @PathVariable groupId: UUID,
                                  @PathVariable notificationId: UUID,
                                  @PathVariable attachmentId: UUID) {
        attachmentManager.unlink(loginId, notificationId, attachmentId)
    }
}