package kr.respectme.group.port.out.msa.member

import kr.respectme.group.port.out.msa.member.dto.PlatformMember
import java.util.UUID

interface LoadPlatformMemberPort {

    fun getMember(memberId: UUID): PlatformMember
}