package kr.respectme.group.domain.notifications

enum class DayOfWeek(val value: Int) {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(4),
    THURSDAY(8),
    FRIDAY(16),
    SATURDAY(32),
    SUNDAY(64)
    ;
    companion object {
        fun toList(dayOfWeeks: Int?): List<DayOfWeek> {
            val dayLists = mutableListOf<DayOfWeek>()
            return dayOfWeeks?.let{
                for (day in DayOfWeek.values()) {
                    if (dayOfWeeks and day.value != 0) {
                        dayLists.add(day)
                    }
                }
                dayLists.toList()
            } ?: emptyList()
        }
    }
}