package ua.od.acros.matusi.data.mapper

import ua.od.acros.matusi.data.model.CareScheduleDto
import ua.od.acros.matusi.domain.model.CareSchedule

class CareScheduleDtoMapper: Mapper<CareSchedule, CareScheduleDto> {
    override fun map(input: CareSchedule) =
        CareScheduleDto(
            day = input.day,
            checked = input.checked,
            start = input.start,
            end = input.end
        )
}