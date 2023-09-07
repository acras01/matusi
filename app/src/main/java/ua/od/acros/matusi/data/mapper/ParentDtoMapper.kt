package ua.od.acros.matusi.data.mapper

import ua.od.acros.matusi.data.model.ParentDto
import ua.od.acros.matusi.domain.model.Parent

class ParentDtoMapper(
    private val childDtoMapper: ChildDtoMapper,
    private val careScheduleDtoMapper: CareScheduleDtoMapper
    ): Mapper<Parent, ParentDto> {

    override fun map(input: Parent) =
        ParentDto(
            id = input.id,
            nickname = input.nickname,
            facebook = input.facebook,
            skype = input.skype,
            viber = input.viber,
            twitter = input.twitter,
            location = input.location,
            radius = input.radius,
            kids = ArrayList(input.kids.map { childDtoMapper.map(it) }.toList()),
            karma = input.karma,
            readyToJoin = input.readyToJoin,
            schedule = ArrayList(input.schedule.map { careScheduleDtoMapper.map(it) }.toList())
        )
}