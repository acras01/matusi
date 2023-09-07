package ua.od.acros.matusi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import kotlin.collections.ArrayList

@Entity(tableName = "parents")
data class ParentDto(
    @PrimaryKey val id: String,
    var nickname: String,
    var facebook: String,
    var skype: String,
    var viber: String,
    var twitter: String,
    var location: LatLng?,
    var radius: Long?,
    var kids: ArrayList<ChildDto> = arrayListOf(),
    var karma: Long,
    var readyToJoin: Boolean,
    var schedule: ArrayList<CareScheduleDto> = arrayListOf()
    )