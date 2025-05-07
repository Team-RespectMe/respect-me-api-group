package kr.respectme.group.domain.notifications

enum class NotificationType(val dbValue: Int) {
    IMMEDIATE( 1),
    SCHEDULED( 2),
    REPEATED_WEEKLY(3),
    REPEATED_INTERVAL(4)
}