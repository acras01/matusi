package ua.od.acros.matusi.data.db

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import ua.od.acros.matusi.data.model.CareScheduleDto
import ua.od.acros.matusi.data.model.ChildDto

class Converters {

    @TypeConverter
    fun fromLocation(location: LatLng): String = "${location.latitude}, ${location.longitude}"

    @TypeConverter
    fun toLocation(data: String): LatLng {
        val temp = data.split(", ")
        return  LatLng(temp[0].toDouble(), temp[1].toDouble())
    }

    @TypeConverter
    fun kidsListToJsonString(value: List<ChildDto>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToKidsList(value: String): ArrayList<ChildDto> {
        val list = Gson().fromJson(value, Array<ChildDto>::class.java).toList()
        return ArrayList(list)
    }

    @TypeConverter
    fun scheduleListToJsonString(value: List<CareScheduleDto>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToScheduleList(value: String): ArrayList<CareScheduleDto> {
        val list = Gson().fromJson(value, Array<CareScheduleDto>::class.java).toList()
        return ArrayList(list)
    }
}