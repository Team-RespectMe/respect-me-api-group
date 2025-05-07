package kr.respectme.group.unit.application

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.respectme.group.application.query.NotificationQueryService
import kr.respectme.group.port.out.persistence.LoadNotificationPort
import java.util.*

internal class NotificationServiceTest: BehaviorSpec({

    val notificationQueryPort = mockk<LoadNotificationPort>()
    val notificationService = NotificationQueryService(notificationQueryPort)

    Given("그룹의 아이디가 주어진다") {
        val groupId = UUID.randomUUID();

        When("그룹에 오늘 발행된 메시지를 조회하면") {
            every { notificationQueryPort.countTodayGroupNotification(groupId) } returns 5

            Then("그룹에 오늘 발행된 메시지의 개수를 반환한다") {
                val result = notificationService.retrieveTodayLeftNotificationCount(groupId)
                result.count shouldBe 5
            }
        }
    }
})