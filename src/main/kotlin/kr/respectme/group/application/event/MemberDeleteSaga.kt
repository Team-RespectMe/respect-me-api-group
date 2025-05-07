package kr.respectme.group.application.event

import java.util.*

class MemberDeleteSaga(
    val eventVersion: Int = 1,
    val memberId: UUID
) {
}