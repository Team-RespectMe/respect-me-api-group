package kr.respectme.group.adapter.out.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PreUpdate
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class CreatedAtUpdatedAtEntity {

    @CreatedDate
    @Column
    var createdAt: Instant = Instant.now()

    @LastModifiedDate
    @Column
    var updatedAt: Instant? = null

    @PreUpdate
    fun updateLastModifiedDate() {
        updatedAt = Instant.now()
    }
}