package kr.respectme.group.port.out.event.dto

import java.util.UUID

class NotificationSentEvent(
    var groupId: UUID = UUID.randomUUID(),
    var notificationId: UUID = UUID.randomUUID(),
    var result: Boolean = true,
    var error: String = "",
    var successCount: Int = 0,
    var failureCount: Int = 0
) {

}