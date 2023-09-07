package ua.od.acros.matusi.data.mapper

import ua.od.acros.matusi.data.model.ParentDto
import ua.od.acros.matusi.domain.model.Parent

class ParentMapper(
    private val childMapper: ChildMapper,
    private val careScheduleMapper: CareScheduleMapper
    ): Mapper<ParentDto, Parent> {

    override fun map(input: ParentDto) =
        Parent(
            id = input.id,
            nickname = input.nickname,
            facebook = input.facebook,
            skype = input.skype,
            viber = input.viber,
            twitter = input.twitter,
            location = input.location,
            radius = input.radius,
            kids = ArrayList(input.kids.map { childMapper.map(it) }.toList()),
            karma = input.karma,
            readyToJoin = input.readyToJoin,
            schedule = ArrayList(input.schedule.map { careScheduleMapper.map(it) }.toList())
        )
}