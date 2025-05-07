package kr.respectme.group.domain.notifications

enum class NotificationStatus(val value: String, val dbValue: Int) {
    PENDING("PENDING", 1),
    PUBLISHED("PUBLISHED", 2),
    FAILED("FAILED", 3),
    ;
}