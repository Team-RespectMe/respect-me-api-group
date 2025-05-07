package kr.respectme.group.port.out.persistence

import kr.respectme.group.domain.NotificationGroup
import java.util.UUID

interface SaveGroupPort {

    fun save(group: NotificationGroup): NotificationGroup

    fun delete(group: NotificationGroup): Unit

    fun deleteById(id: UUID): Unit
}