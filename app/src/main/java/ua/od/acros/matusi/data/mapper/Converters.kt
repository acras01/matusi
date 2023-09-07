package ua.od.acros.matusi.data.mapper

import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import ua.od.acros.matusi.data.model.CareScheduleDto
import ua.od.acros.matusi.data.model.ChildDto

internal fun LatLng.fromLocation(): String = "${this.latitude}, ${this.longitude}"

internal fun String.toLocation(): LatLng {
    val temp = this.split(", ")
    return  LatLng(temp[0].toDouble(), temp[1].toDouble())
}

internal fun String.toKidsList(): ArrayList<ChildDto> {
    val list = Gson().fromJson(this, Array<ChildDto>::class.java).toList()
    return ArrayList(list)
}

internal fun String.toScheduleList(): ArrayList<CareScheduleDto> {
    val list = Gson().fromJson(this, Array<CareScheduleDto>::class.java).toList()
    return ArrayList(list)
}

internal fun List<Any>.toJsonString(): String = Gson().toJson(this)