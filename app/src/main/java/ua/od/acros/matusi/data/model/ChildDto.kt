package ua.od.acros.matusi.data.model

data class ChildDto(
    val id: String,
    val name: String,
    val age: Long
) {
    fun toStringCustom(): String {
        return "$name, $age"
    }
}