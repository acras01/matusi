package ua.od.acros.matusi.data.mapper

import ua.od.acros.matusi.data.model.ChildDto
import ua.od.acros.matusi.domain.model.Child

class ChildDtoMapper: Mapper<Child, ChildDto> {
    override fun map(input: Child) =
        ChildDto(
            id = input.id,
            name = input.name,
            age = input.age
        )
}