package kr.respectme.group.application.command

import kr.respectme.common.error.ForbiddenException
import kr.respectme.common.error.NotFoundException
import kr.respectme.group.application.command.useCase.NotificationGroupCommandUseCase
import kr.respectme.group.application.dto.group.GroupCreateCommand
import kr.respectme.group.application.dto.group.GroupModifyCommand
import kr.respectme.group.application.dto.group.NotificationGroupDto
import kr.respectme.group.common.errors.GroupServiceErrorCode.*
import kr.respectme.group.domain.GroupMember
import kr.respectme.group.domain.GroupMemberRole
import kr.respectme.group.domain.NotificationGroup
import kr.respectme.group.port.out.persistence.LoadGroupPort
import kr.respectme.group.port.out.persistence.SaveGroupPort
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class NotificationGroupCommandService(
    private val loadGroupPort: LoadGroupPort,
    private val saveGroupPort: SaveGroupPort,
    private val passwordEncoder: PasswordEncoder
): NotificationGroupCommandUseCase {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun createNotificationGroup(loginId: UUID, command: GroupCreateCommand): NotificationGroupDto {
        val group = NotificationGroup(
            name = command.groupName,
            description = command.groupDescription,
            type = command.groupType,
            thumbnail = command.groupImageUrl
        )

        val owner = GroupMember(
            memberId = loginId,
            groupId = group.id,
            nickname = command.groupOwnerNickname,
            memberRole = GroupMemberRole.OWNER
        )

        logger.info("[GroupCreateEvent] group create request member : ${loginId}\n command : $command")
        group.changePassword(passwordEncoder, command.groupPassword)
        group.setOwner(owner)
        val savedGroup = saveGroupPort.save(group)
        logger.info("[GroupCreateEvent] platform member : ${loginId} created new group : ${savedGroup.id} group name: ${savedGroup.getName()}")
        return NotificationGroupDto.valueOf(savedGroup)
    }

    @Transactional
    override fun updateNotificationGroup(loginId: UUID, groupId: UUID, command: GroupModifyCommand): NotificationGroupDto {
        val group = loadGroupPort.loadGroup(
            groupId = groupId
        ) ?: throw NotFoundException(GROUP_NOT_FOUND)
        logger.info("[GroupModifyEvent] group modify request member : ${loginId}\n command : $command")

        if(group.getOwner().getMemberId() != loginId) {
            throw ForbiddenException(GROUP_MEMBER_NOT_OWNER)
        }

        group.changeGroupName(command.name)
        group.changeGroupType(command.type)
        group.changeGroupDescription(command.description)
        group.changePassword(passwordEncoder, command.password)
        group.changeThumbnail(command.thumbnail)

        logger.info("[GroupModifyEvent] group modify request accepted, modified by member : ${loginId}" +
                "group name : ${group.getName()} group type : ${group.getType()} group description : ${group.getDescription()}")

        return NotificationGroupDto.valueOf(saveGroupPort.save(group))
    }

    @Transactional
    override fun deleteNotificationGroup(loginId: UUID, groupId: UUID) {
        logger.info("[GroupDeleteEvent] group delete request member : ${loginId} group id : ${groupId}")
        val group = loadGroupPort.loadGroup(groupId)
            ?: throw NotFoundException(GROUP_NOT_FOUND)

        if(group.getOwnerId() != loginId) {
            logger.error("[GroupDeleteEvent] group delete request rejected, member : ${loginId} is not group owner. owner Id ${group.getOwnerId()}")
            throw ForbiddenException(GROUP_MEMBER_NOT_OWNER)
        }
        saveGroupPort.delete(group)
        logger.info("[GroupDeleteEvent] group delete request accepted, member : ${loginId} deleted group ${groupId} group name : ${group.getName()}.")
    }
}