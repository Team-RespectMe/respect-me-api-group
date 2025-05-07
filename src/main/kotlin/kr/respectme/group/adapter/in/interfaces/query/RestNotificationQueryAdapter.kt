package kr.respectme.group.adapter.`in`.interfaces.query

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import kr.respectme.common.annotation.ApplicationResponse
import kr.respectme.common.annotation.CursorPagination
import kr.respectme.common.annotation.CursorParam
import kr.respectme.common.annotation.LoginMember
//import kr.respectme.group.application.AttachedFileService
import kr.respectme.group.application.dto.notification.NotificationCountDto
import kr.respectme.group.application.query.useCase.NotificationGroupQueryUseCase
import kr.respectme.group.application.query.useCase.NotificationQueryUseCase
import kr.respectme.group.port.`in`.interfaces.NotificationQueryPort
import kr.respectme.group.port.`in`.interfaces.dto.GroupNotificationQueryResponse
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/")
@Tags(Tag(name = "Notification Query API", description = "그룹 알림 조회 API, 알림의 조회를 담당 합니다."))
class RestNotificationQueryAdapter(
    private val notificationQueryUsecase: NotificationQueryUseCase,
//    private val attachedFileUseCase: AttachedFileService
): NotificationQueryPort {

    @GetMapping("notification-groups/{groupId}/limit-status")
    @Operation(summary = "오늘 그룹에서 추가 발행 가능한 알림 수 반환", description = "오늘 발송된 그룹의 잔여 알림 수를 반환한다. 시간은 UTC 기준이며, YYYY-MM-DD 00:00:00 ~ 23:59:59 까지의 발행된 알림 수를 반환한다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "그룹의 잔여 알림 개수 조회 성공"),
        ApiResponse(responseCode = "403", description = "그룹에 대한 권한이 없음"),
    ])
    @ApplicationResponse(status = HttpStatus.OK, message = "group notification left count retrieved.")
    override fun getTodayLeftNotificationCount(
        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID): NotificationCountDto {
        return notificationQueryUsecase.retrieveTodayLeftNotificationCount(groupId)
    }

    @GetMapping("notification-groups/{groupId}/notifications")
    @Operation(summary = "지정된 Group 의 알림 목록을 반환.", description = "지정된 Group 의 알림 목록을 CursorList 타입으로 래핑하여 반환합니다." +
            "<br/> CursorList 는 다음 페이지가 존재하면 next 필드에 다음 페이지의 URL을 포함합니다." +
            "<br/> 제공된 Access Token의 MemberId(sub)가 Group에 존재하지 않으면 403 Forbidden을 반환합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "그룹의 알림 목록 조회 성공"),
        ApiResponse(responseCode = "403", description = "그룹에 대한 권한이 없음"),
    ])
    @ApplicationResponse(status = HttpStatus.OK, message = "group notifications retrieved.")
    @CursorPagination
    override fun getGroupNotifications(
        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID,
        @RequestParam(required = false) @CursorParam(key="notificationId") cursor: UUID?,
        @RequestParam(required = false, defaultValue = "20") size: Int?
    ): List<GroupNotificationQueryResponse> {
        return notificationQueryUsecase.retrieveGroupNotifications(loginId, groupId, cursor, size?:20 )
            .map { it -> GroupNotificationQueryResponse.valueOf(it) }
    }

    @GetMapping("notification-groups/{groupId}/notifications/{notificationId}")
    @Operation(summary = "그룹의 단일 알림을 상세 조회합니다.", description = "이 API를 사용해서 조회하는 경우 content 필드가 요약되지 않고 전체 내용을 포함합니다")
    @ApiResponses(value =[
        ApiResponse(responseCode = "200", description = "그룹의 단일 노티피케이션 조회 성공"),
        ApiResponse(responseCode = "403", description = "그룹에 대한 권한이 없음"),
        ApiResponse(responseCode = "404", description = "노티피케이션을 찾을 수 없음")
    ])
    @ApplicationResponse(status = HttpStatus.OK, message = "group notification retrieved.")
    override fun getNotification(
        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID,
        @PathVariable notificationId: UUID
    ): GroupNotificationQueryResponse {
        val response =  GroupNotificationQueryResponse.valueOf(
            notificationQueryUsecase.retrieveNotification(loginId, groupId, notificationId)
        )

//        response.attachedFiles = attachedFileUseCase.getAttachedFiles(notificationId)
//            .map { it -> GroupNotificationQueryResponse.AttachedFile(it.fileId) }

        return response
    }

    @GetMapping("group-members/me/notifications")
    @Operation(summary = "내가 속한 그룹의 모든 알림을 조회합니다.", description = "내가 속한 그룹의 모든 알림을 조회합니다.")
    @ApplicationResponse(status = HttpStatus.OK, message = "member notifications retrieved.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "내가 속한 그룹의 모든 알림 조회 성공")
    ])
    @CursorPagination
    override fun getMemberNotifications(
        @LoginMember loginId: UUID,
        @RequestParam(required = false) @CursorParam(key = "notificationId") cursor: UUID?,
        @RequestParam(defaultValue = "20", required = false) size : Int?
    ): List<GroupNotificationQueryResponse> {
        return notificationQueryUsecase.retrieveUnreadNotifications(loginId, cursor, size?:20)
            .map { it -> GroupNotificationQueryResponse.valueOf(it) }
            .toList()
    }
}