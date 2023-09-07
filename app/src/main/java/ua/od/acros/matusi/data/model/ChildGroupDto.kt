package ua.od.acros.matusi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChildGroupDto(
    @PrimaryKey val id: String,
    val kids: List<ChildDto>
    )