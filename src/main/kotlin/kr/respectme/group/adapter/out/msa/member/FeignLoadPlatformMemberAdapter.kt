package kr.respectme.group.adapter.out.msa.member

import kr.respectme.group.port.out.msa.member.dto.PlatformMember
import kr.respectme.group.port.out.msa.member.LoadPlatformMemberPort
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.*

@FeignClient(name = "member-api", url = "\${respect-me.msa.member-api.url}")
interface FeignLoadPlatformMemberAdapter: LoadPlatformMemberPort {

    @GetMapping("/api/v1/members/{memberId}")
    override fun getMember(@PathVariable memberId: UUID): PlatformMember
}