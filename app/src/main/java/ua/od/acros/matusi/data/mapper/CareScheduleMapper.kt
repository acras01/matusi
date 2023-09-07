package ua.od.acros.matusi.data.mapper

import ua.od.acros.matusi.data.model.CareScheduleDto
import ua.od.acros.matusi.domain.model.CareSchedule

class CareScheduleMapper: Mapper<CareScheduleDto, CareSchedule> {
    override fun map(input: CareScheduleDto) =
        CareSchedule(
            day = input.day,
            checked = input.checked,
            start = input.start,
            end = input.end
        )
}