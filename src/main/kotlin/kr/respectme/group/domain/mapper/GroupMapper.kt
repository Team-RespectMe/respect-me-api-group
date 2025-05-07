package kr.respectme.group.domain.mapper

import kr.respectme.group.adapter.out.persistence.entity.JpaGroupMember
import kr.respectme.group.adapter.out.persistence.entity.JpaNotificationGroup
import kr.respectme.group.adapter.out.persistence.entity.notifications.JpaGroupNotification
import kr.respectme.group.domain.GroupMember
import kr.respectme.group.domain.NotificationGroup
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class GroupMapper(
    private val memberMapper: GroupMemberMapper,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun toDomain(group: JpaNotificationGroup, groupOwner: JpaGroupMember): NotificationGroup {
        // Persistence Entity가 Domain Entity로 변환이 된다는 것은, 기본적으로 영속화가 되어 id가 존재한다는 의미이다.
        // id field는 기본적으로 nullable 이지만, 영속화 된 id field를 조회하기위해 identifier를 이용한다.
        val domainGroup = NotificationGroup(
            id = group.identifier,
            name = group.name,
            description = group.description,
            owner = memberMapper.toDomain(groupOwner),
            password = group.password,
            type = group.type,
            thumbnail = group.thumbnail
        )
        logger.debug("cast completed")

        return domainGroup
    }

    fun toEntity(group: NotificationGroup): JpaNotificationGroup {
        // 도메인 엔티티의 상태를 체크해야한다.
        // 신규 생성된 엔티티의 경우 id필드를 null 로 세팅한다.
        // NEW 외의 상태에서는 id필드를 유지한다.
        val entity = JpaNotificationGroup(
            id = group.id,
            name = group.getName(),
            description = group.getDescription(),
            ownerId = group.getOwnerId(),
            password = group.getPassword(),
            type = group.getType(),
            thumbnail = group.getThumbnail()
        )

        return entity
    }
}