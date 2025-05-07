package kr.respectme.group.adapter.`in`.saga

import kr.respectme.common.saga.SagaEvent
import kr.respectme.common.saga.SagaEventHandler
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupMemberRepository
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupRepository
import kr.respectme.group.application.event.MemberDeleteSaga
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class MemberDeleteSagaEventHandler(
    private val groupRepository: JpaGroupRepository,
    private val groupMemberRepository: JpaGroupMemberRepository,
) : SagaEventHandler<SagaEvent<MemberDeleteSaga>>() {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun handle(event: SagaEvent<MemberDeleteSaga>) {
        updateGroupDeletedStatus(event.data!!.memberId, true)
        updateGroupMemberDeletedStatus(event.data!!.memberId, true)
    }

    @Transactional
    override fun compensate(event: SagaEvent<MemberDeleteSaga>) {
        updateGroupMemberDeletedStatus(event.data!!.memberId, false)
        updateGroupDeletedStatus(event.data!!.memberId, false)
    }

    @Transactional
    private fun updateGroupMemberDeletedStatus(memberId: UUID, status: Boolean) {
        groupMemberRepository.softDeleteByMemberId(memberId)
    }

    @Transactional
    private fun updateGroupDeletedStatus(memberId: UUID, status: Boolean) {
        groupRepository.softDeleteByOwnerId(memberId)
    }
}