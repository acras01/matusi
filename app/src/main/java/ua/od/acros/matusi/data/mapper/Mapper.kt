package ua.od.acros.matusi.data.mapper

interface Mapper<Input, Output> {
    fun map(input: Input): Output?
}