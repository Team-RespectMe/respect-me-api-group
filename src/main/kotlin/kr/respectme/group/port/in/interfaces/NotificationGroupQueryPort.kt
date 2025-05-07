package kr.respectme.group.port.`in`.interfaces

import kr.respectme.group.port.`in`.interfaces.vo.GroupMemberVo
import kr.respectme.group.port.`in`.interfaces.dto.GroupNotificationQueryResponse
import kr.respectme.group.port.`in`.interfaces.dto.NotificationGroupQueryResponse
import java.util.UUID

interface NotificationGroupQueryPort {

    /**
     * 그룹의 요약 정보를 조회합니다.
     * @param loginId: Access Token을 통해 확보한 사용자 ID
     * @param groupId: 조회할 그룹 ID
     * @return NotificationGroupSummaryDto
     */
    fun getNotificationGroup(loginId: UUID, groupId: UUID): NotificationGroupQueryResponse

    /**
     * 내가 속한 그룹을 조회합니다.
     * @param loginId: Access Token을 통해 확보한 사용자 ID
     * @param cursor: 다음 페이지를 조회하기 위한 커서 Group ID에 해당한다.
     * @param size: 한 페이지에 조회할 그룹 수
     * @return List<NotificationGroupVo>
     */
    fun getMyGroups(loginId: UUID, cursor: UUID?, size: Int?): List<NotificationGroupQueryResponse>

//    /**
//     * 모든 그룹을 조회합니다.
//     * @param loginId: Access Token을 통해 확보한 사용자 ID
//     * @param cursor: 다음 페이지를 조회하기 위한 커서 Group ID에 해당한다.
//     * @param size: 한 페이지에 조회할 그룹 수
//     * @return List<NotificationGroupVo>
//    **/
//    fun getAllGroups(loginId: UUID, cursor: UUID?, size: Int?): List<NotificationGroupQueryResponse>

    /**
     * 그룹 목록을 조회합니다, name에 해당하는 키워드가 이름에 포함되어 있을 경우 조회합니다.
     * @param loginId: Access Token을 통해 확보한 사용자 ID
     * @param keyword: 그룹 이름 검색 키워드
     * @param cursor: 다음 페이지를 조회하기 위한 커서 Group ID에 해당한다.
     * @param size: 한 페이지에 조회할 그룹 수aaㅁㅁ
     * @return List<NotificationGroupVo>
     */
    fun getGroupsBySearchParams(loginId: UUID, keyword: String, cursor: UUID?, size: Int?): List<NotificationGroupQueryResponse>
}