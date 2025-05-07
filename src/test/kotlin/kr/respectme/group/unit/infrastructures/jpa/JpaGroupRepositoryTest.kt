package kr.respectme.group.unit.infrastructures.jpa

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import kr.respectme.group.adapter.out.persistence.entity.JpaNotificationGroup
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupMemberRepository
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupRepository
import kr.respectme.group.support.createJpaGroup
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.transaction.annotation.Transactional
import java.util.*

@DataJpaTest
internal class JpaGroupRepositoryTest(
    private val groupRepository: JpaGroupRepository,
    private val groupMemberRepository: JpaGroupMemberRepository
)
: AnnotationSpec() {

    init {
        extension(SpringExtension)
    }

    var groups: MutableList<JpaNotificationGroup> = mutableListOf()
    var groupIds: MutableList<UUID> = mutableListOf()

    @BeforeEach
    fun setUp() {
        val newGroups = createJpaGroup(5)
        newGroups.forEachIndexed { index, group ->
            println(group.id)
        }

        groups = newGroups.map{ groupRepository.save(it) }.toMutableList()
        groupIds = MutableList(groups.size){ groups[it].identifier }

    }

    @Test
    @Transactional
    fun`Group 생성 테스트`() {
        val group = createJpaGroup(1).first()
        val saved = groupRepository.save(group)

        println("Group Id : ${group.id}")
        println("Group Name : ${group.name}")
        println("Group Description : ${group.description}")
        println("Group Type : ${group.type}")
    }

    @Test
    @Transactional
    fun `Group 조회 테스트`() {
        val group = groupRepository.findById(groupIds[0])
        group?.id shouldBe  groupIds[0]
        group?.name shouldBe "group-name-${groupIds[0]}"
    }

    @AfterEach
    fun cleanUp() {
        groups.clear()
        groupIds.clear()
        groupIds.forEach { groupRepository.deleteById(it) }
    }

}