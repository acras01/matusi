package ua.od.acros.matusi.domain.model

import ua.od.acros.matusi.presentation.misc.getWeekDayNameLocalized

class CareSchedule(
    val day: Int,
    var checked: Boolean,
    var start: String,
    var end: String
) {
    fun toStringCustom(): String {
        return if (checked)
            "${getWeekDayNameLocalized(day)}, $start - $end"
        else
            ""
    }
}