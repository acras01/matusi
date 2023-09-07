package ua.od.acros.matusi.domain.model

data class Child(
    val id: String,
    val name: String,
    val age: Long
) {
    fun toStringCustom(): String {
        return "$name, $age"
    }
}