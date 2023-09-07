package ua.od.acros.matusi.data.mapper

import ua.od.acros.matusi.data.model.ChildDto
import ua.od.acros.matusi.domain.model.Child

class ChildMapper: Mapper<ChildDto, Child> {
    override fun map(input: ChildDto) =
        Child(
            id = input.id,
            name = input.name,
            age = input.age
        )
}