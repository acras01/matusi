package ua.od.acros.matusi.data.model

import ua.od.acros.matusi.presentation.misc.getWeekDayNameLocalized

class CareScheduleDto(
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