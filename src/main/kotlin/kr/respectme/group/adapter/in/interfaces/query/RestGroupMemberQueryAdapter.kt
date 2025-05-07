package kr.respectme.group.adapter.`in`.interfaces.query

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kr.respectme.common.annotation.ApplicationResponse
import kr.respectme.common.annotation.CursorPagination
import kr.respectme.common.annotation.CursorParam
import kr.respectme.common.annotation.LoginMember
import kr.respectme.group.application.query.useCase.GroupMemberQueryUseCase
import kr.respectme.group.port.`in`.interfaces.GroupMemberQueryPort
import kr.respectme.group.port.`in`.interfaces.vo.GroupMemberVo
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/")
@Tag(name = "Group Member Query API", description = "그룹의 멤버 조회 API, 그룹의 멤버 조회를 담당 합니다.")
class RestGroupMemberQueryAdapter(
    private val groupMemberQueryUseCase: GroupMemberQueryUseCase
): GroupMemberQueryPort {

    @GetMapping("notification-groups/{groupId}/members")
    @Operation(summary = "지정된 Group 의 회원 목록을 반환.", description = "지정된 Group 의 회원 목록을 CursorList 타입으로 래핑하여 반환합니다." +
            "<br/> CursorList 는 다음 페이지가 존재하면 next 필드에 다음 페이지의 URL을 포함합니다." +
            "<br/> 제공된 Access Token의 MemberId(sub)가 Group에 존재하지 않으면 403 Forbidden을 반환합니다.")
    @ApplicationResponse(status = HttpStatus.OK, message = "group members retrieved.")
    @CursorPagination
    override fun getGroupMembers(
        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID,
        @RequestParam(required = false) @CursorParam("id") cursor: UUID?,
        @RequestParam(required = false, defaultValue = "20") size: Int?): List<GroupMemberVo> {
        return groupMemberQueryUseCase.retrieveGroupMembers(loginId, groupId, cursor, size ?: 20)
            .map { it -> GroupMemberVo.valueOf(it) }
    }

    @GetMapping("notification-groups/{groupId}/members/{memberId}")
    @ApplicationResponse(status = HttpStatus.OK, message = "group member retrieved.")
    @Operation(summary = "지정된 Group 의 회원을 반환.", description = "지정된 Group 의 회원 정보를 반환합니다." +
            "<br/> 제공된 Access Token의 MemberId(sub)가 Group에 존재해야 하며 그렇지 않을 경우 403 Forbidden이 반환됩니다.")
    override fun getGroupMember(
        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID,
        @PathVariable memberId: UUID): GroupMemberVo {
        return GroupMemberVo.valueOf(
            groupMemberQueryUseCase.retrieveGroupMember(loginId, groupId, memberId)
        )
    }
}