package kr.respectme.group.unit.adapter.out

import com.querydsl.jpa.impl.JPAQueryFactory
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import kr.respectme.group.adapter.out.persistence.JpaLoadNotificationAdapter
import kr.respectme.group.adapter.out.persistence.entity.JpaGroupMember
import kr.respectme.group.adapter.out.persistence.entity.JpaNotificationGroup
import kr.respectme.group.adapter.out.persistence.entity.notifications.JpaGroupNotification
import kr.respectme.group.adapter.out.persistence.entity.notifications.JpaImmediateNotification
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupMemberRepository
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupNotificationRepository
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupRepository
import kr.respectme.group.domain.GroupMemberRole
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.support.config.TestConfig
import kr.respectme.group.support.createJpaGroup
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import java.util.UUID

@DataJpaTest
@Import(TestConfig::class)
class JpaQueryNotificationAdapterTest(
    private val jpaGroupRepository: JpaGroupRepository,
    private val jpaMemberRepository: JpaGroupMemberRepository,
    private val jpaNotificationRepository: JpaGroupNotificationRepository,
    private val qf: JPAQueryFactory,
) : AnnotationSpec() {

    private lateinit var members: List<JpaGroupMember>
    private lateinit var groups: List<JpaNotificationGroup>
    private lateinit var notifications: List<JpaGroupNotification>
    private val jpaQueryNotificationAdapter = JpaLoadNotificationAdapter(qf)

    override fun extensions(): List<Extension> {
        return listOf(SpringExtension)
    }

    @BeforeEach
    fun setUp() {
        groups = createJpaGroup(1)
        members = groups.map{ group ->
            JpaGroupMember(
                id = UUID.randomUUID(),
                groupId = group.identifier,
                memberId = UUID.randomUUID(),
                nickname = "owner",
                memberRole = GroupMemberRole.OWNER,
            )
        }
        notifications = listOf(
            JpaImmediateNotification(
                id = UUID.randomUUID(),
                groupId = groups[0].identifier,
                memberId = members[0].memberId,
                content = "content",
                status = NotificationStatus.PENDING,
            ),
            JpaImmediateNotification(
                id = UUID.randomUUID(),
                groupId = groups[0].identifier,
                memberId = members[0].memberId,
                content = "content",
                status = NotificationStatus.PENDING,
            ),
            JpaImmediateNotification(
                id = UUID.randomUUID(),
                groupId = groups[0].identifier,
                memberId = members[0].memberId,
                content = "content",
                status = NotificationStatus.PENDING,
            )
        )

        jpaGroupRepository.save(groups[0])
        members.forEach { it -> jpaMemberRepository.save(it) }
        notifications.forEach { it -> jpaNotificationRepository.save(it) }
    }

    @Test()
    fun `그룹에 발행된 오늘 메시지 수를 조회한다`() {
        val result = jpaQueryNotificationAdapter.countTodayGroupNotification(groups[0].identifier)
        result shouldBe 3
    }

    @AfterEach
    fun tearDown() {
        jpaNotificationRepository.deleteAll()
        jpaMemberRepository.deleteAll()
        jpaGroupRepository.deleteByGroupId(groups[0].identifier)
    }
}