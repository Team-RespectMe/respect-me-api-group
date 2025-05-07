package kr.respectme.group.application.attachment

import kr.respectme.group.domain.attachment.AttachmentType
import java.util.UUID


interface AttachmentHandler {

    /**
     * Attachment와 Notification을 연결하는 메서드
     * @param loginId 로그인 한 사용자 식별자
     * @param command 첨부 리소스 링크 요청
     * @return AttachmentDto
     */
    fun linkAttachment(loginId: UUID, command: LinkAttachmentCommand): AttachmentDto

    /**
     * Attachment을 지원하는지 확인하는 메서드
     * @param command 첨부 리소스 링크 요청
     * @return Boolean
     */
    fun isSupport(command: LinkAttachmentCommand): Boolean

    /**
     * Attachment을 지원하는지 확인하는 메서드
     * @param type Attachment Type
     * @return Boolean
     */
    fun isSupport(type: AttachmentType): Boolean
}