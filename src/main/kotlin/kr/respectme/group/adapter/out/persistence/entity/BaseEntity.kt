package kr.respectme.group.adapter.out.persistence.entity

import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy
import org.springframework.data.domain.Persistable
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.UUID

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity<T>(id: UUID?=null): CreatedAtUpdatedAtEntity(), Persistable<UUID> {

    @Id
    private val id: UUID? = id

    override fun getId(): UUID? {
        return id
    }

    val identifier: UUID
        get() = id!!

    @Transient
    private var _isNew = false

    fun created() {
        _isNew = true
    }

    override fun isNew(): Boolean {
        if(id == null) return true
        return _isNew
    }

    @PostPersist
    @PostLoad
    protected fun load() {
        _isNew = false
    }

    override fun equals(other: Any?): Boolean {
        if(other == null) return false
        if(other !is HibernateProxy && this::class != other::class) return false
        return id == (other as BaseEntity<Any?>).id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}