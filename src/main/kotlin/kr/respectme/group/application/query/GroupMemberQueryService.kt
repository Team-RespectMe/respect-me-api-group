package kr.respectme.group.application.query

import kr.respectme.common.error.NotFoundException
import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.application.query.useCase.GroupMemberQueryUseCase
import kr.respectme.group.common.errors.GroupServiceErrorCode
import kr.respectme.group.port.out.persistence.LoadMemberPort
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class GroupMemberQueryService(
    private val loadMemberPort: LoadMemberPort
): GroupMemberQueryUseCase {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun retrieveGroupMembers(loginId: UUID, groupId: UUID, cursor: UUID?, size: Int): List<GroupMemberDto> {
        val members = loadMemberPort.findAllByGroupId(groupId, cursor, PageRequest.of(0,  size + 1))
        logger.debug("group members query result : ${members.content.size}")

        return members.content.map { entity -> GroupMemberDto.valueOf(entity) }
    }

    @Transactional(readOnly = true)
    override fun retrieveGroupMember(loginId: UUID, groupId: UUID, memberId: UUID): GroupMemberDto {
        val member = loadMemberPort.findByGroupIdAndMemberId(groupId, memberId)
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_MEMBER_NOT_FOUND)

        return GroupMemberDto.valueOf(member)
    }
}