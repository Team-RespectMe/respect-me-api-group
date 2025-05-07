package kr.respectme.group.adapter.`in`.interfaces.query

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import kr.respectme.common.annotation.ApplicationResponse
import kr.respectme.common.annotation.CursorPagination
import kr.respectme.common.annotation.CursorParam
import kr.respectme.common.annotation.LoginMember
import kr.respectme.group.application.dto.group.GroupSearchParams
import kr.respectme.group.application.query.useCase.NotificationGroupQueryUseCase
import kr.respectme.group.port.`in`.interfaces.vo.GroupMemberVo
import kr.respectme.group.port.`in`.interfaces.dto.GroupNotificationQueryResponse
import kr.respectme.group.port.`in`.interfaces.dto.NotificationGroupQueryResponse
import kr.respectme.group.port.`in`.interfaces.NotificationGroupQueryPort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID


@RestController
@RequestMapping("/api/v1/")
@Tag(name = "Group Query API", description = "알림 그룹의 조회 API, 그룹의 조회를 담당 합니다.")
@SecurityRequirement(name = "bearer-jwt")
class RestGroupQueryAdapter(private val queryUseCase: NotificationGroupQueryUseCase)
: NotificationGroupQueryPort {

    @GetMapping("group-members/me/notification-groups")
    @Operation(summary = "내가 속한 그룹 목록을 반환.", description = "내가 속한 그룹 목록을 CursorList 타입으로 래핑하여 반환합니다." +
            "<br/> CursorList 는 다음 페이지가 있는지 여부와 다음 페이지의 Cursor(next) 를 포함합니다." +
            "<br/> 제공된 Access Token의 MemberId(sub)가 Group에 존재하지 않으면 403 Forbidden을 반환합니다.")
    @CursorPagination
    @ApplicationResponse(status = HttpStatus.OK, message = "my groups retrieved.")
    override fun getMyGroups(
        @LoginMember loginId: UUID,
        @RequestParam(required = false) @CursorParam(key="id") cursor: UUID?,
        @RequestParam(required = false, defaultValue = "20") size: Int?
    ): List<NotificationGroupQueryResponse> {
        return queryUseCase.retrieveMemberGroups(loginId)
            .map { it -> NotificationGroupQueryResponse.valueOf(it) }
    }

    @GetMapping("notification-groups/{groupId}")
    @Operation(summary = "지정된 Group 의 정보를 반환.", description = "지정된 Group 의 정보를 반환합니다." +
            "<br/> 제공된 Access Token의 MemberId(sub)가 Group에 존재하지 않으면 403 Forbidden을 반환합니다.")
    @ApplicationResponse(status = HttpStatus.OK, message = "group retrieved.")
    override fun getNotificationGroup(
        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID
    ): NotificationGroupQueryResponse {
        return NotificationGroupQueryResponse.valueOf(
            queryUseCase.retrieveGroup(loginId, groupId)
        )
    }

//    @GetMapping("notification-groups")
//    @Operation(summary = "모든 그룹 목록을 반환.", description = "모든 그룹 목록을 CursorList 타입으로 래핑하여 반환합니다." +
//            "<br/> CursorList 는 다음 페이지가 있을 경우 next 필드에 다음 페이지 조회 URL을 포함합니다.")
//    @ApplicationResponse(status = HttpStatus.OK, message = "all groups retrieved.")
//    @CursorPagination
//    override fun getAllGroups(
//        @LoginMember loginId: UUID,
//        @CursorParam(key="id") @RequestParam(required = false) cursor: UUID?,
//        @RequestParam(required = false, defaultValue = "20") size: Int?
//    ): List<NotificationGroupQueryResponse> {
//        return queryUseCase.retrieveAllGroups(loginId, cursor, size)
//            .map { it -> NotificationGroupQueryResponse.valueOf(it) }
//    }

    @GetMapping("notification-groups")
    @Operation(summary = "그룹 검색 결과 목록을 반환.", description = "검색 대상 그룹을 CursorList 타입으로 래핑하여 반환합니다." +
            "<br/> CursorList 는 다음 페이지가 있을 경우 next 필드에 다음 페이지 조회 URL을 포함합니다.")
    @ApplicationResponse(status = HttpStatus.OK, message = "all groups retrieved.")
    @CursorPagination
    override fun getGroupsBySearchParams(
        @LoginMember loginId: UUID,
        @RequestParam(required = true)
        keyword: String,
        @RequestParam(required = false)
        cursor: UUID?,
        @RequestParam(defaultValue = "20") size: Int?
    ): List<NotificationGroupQueryResponse> {
        return queryUseCase.retrieveGroupsBySearchParam(GroupSearchParams(keyword = keyword), cursor, size)
            .map { it -> NotificationGroupQueryResponse.valueOf(it) }
    }
}